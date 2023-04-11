import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import TfidfTransformer, TfidfVectorizer
from scipy import spatial
from sklearn.metrics.pairwise import cosine_similarity
import random
from collections import defaultdict
import sys
import pymongo


def getSimMatrix(df):
    tfIdfVectorizer = TfidfVectorizer()
    X = tfIdfVectorizerLoc.fit_transform(df)
    similarities = cosine_similarity(X)
    similarities_sparse = cosine_similarity(X,dense_output=False)
    return similarities_sparse

def randCentroids(students,k):
    centroidID = random.sample(list(students),k)
    return centroidID

def getMatches(df,u,Name,Loc,Time,Day):
    matches = []
    nameMatches = getOnes(Name,u)
    locMatches = getOnes(Loc,u)
    timeMatches = getOnes(Time,u)
    dayMatches = getOnes(Day,u)
    for k in range(len(nameMatches)):
        if nameMatches[k] in locMatches and nameMatches[k] in timeMatches and nameMatches[k] in dayMatches:
            matches.append(nameMatches[k])
    return matches

def getOnes(sim,course):
    ind = sim[course].indices
    ans =[]
    for i in ind:
        if sim[course,i] >= 1.0:
            ans.append(i)
    return ans

def findEqivRow(df,df2,ind):
    index = df.index
    condition = df2[(df2["ID"].isin([df["ID"].iloc[ind]])) & (df2['Class Name'] == df.iloc[ind]["Class Name"]) & (df2['Class Loc'] == df.iloc[ind]["Class Loc"]) & (df2['Class Time'] == df.iloc[ind]["Class Time"]) & (df2['Days'] == df.iloc[ind]["Days"])].ID
    otherInd = condition.index.tolist()
    return otherInd

def findMatch(df,course,studentID,nameSim,locSim,timeSim,daySim):
    matches = getMatches(df,course,nameSim,locSim,timeSim,daySim)
    for i in matches:
        if df.iloc[i].ID == studentID:
            return i
        
def matchAll(df,k,threshold,count):
    def helper(df,k,threshold,count):
        nonlocal MATCHES
        tempDF = df.copy(deep=True) #dataframe to update for recursive call
        students = df.ID.unique() #list of user id
        centroidID = randCentroids(students,k) #list of user id of all centroids
        centroidID = sorted(centroidID) #sort the centroids for the first pass
        noCents = [x for x in students if x not in centroidID] #list of noncentroid students
        g = [] # list of lists of IDs that match
        for i in range(k):
            g.append([])
        totalMatches = []
        groups = {}

        for i in range(k):
            groups[(centroidID[i], str(df.loc[df["ID"] == centroidID[i]]["Student Name"].iloc[0]))] = [[], defaultdict(set)]

        #indexes of centroids for matches
        index = df.index
        condition = df['ID'].isin(centroidID)
        centroidInd = index[condition].tolist()
        counts = dict((el,0) for el in centroidID)

        #If there are meaningful matches within the centroids
        if threshold >= 2:


            for centroid in centroidID:
                totalMatches = [] #list of all matches for each student

                counts = dict((el,0) for el in centroidID) #Counts dictionary keeps track of matches

                #Get the indexes of the classes for the current student
                index = df.index
                condition = df['ID'].isin([centroid])
                courses = index[condition].tolist()

                #Run the similarity matrix for all their features
                nameSim = getSimMatrix(df['Class Name'])
                locSim = getSimMatrix(df['Class Loc'])
                timeSim = getSimMatrix(df['Class Time'])
                daySim = getSimMatrix(df['Days'])

                #For each class each that the student has
                for course in courses:

                    matches = getMatches(df,course,nameSim,locSim,timeSim,daySim) #Class matches with other students

                    #check to see if the match is a centroid otherwise discard it
                    for match in matches:
                        if match in centroidInd and df.iloc[match].ID != centroid:

                            cID = df.iloc[match].ID
                            totalMatches.append(match) #append to our totalMatches
                            counts[cID] +=1  #Increment the counts dictionary for the centroid

                bestCluster = max(counts, key = counts.get) #id of the centroid with best match

                #checking which centroid to group to
                for i in range(k):

                    if bestCluster == centroidID[i] and len(g[i]) < 10 and counts[bestCluster] >= threshold: #conditions are met
                        stu = str(df.loc[df["ID"] == centroid]["Student Name"].iloc[0])
                        g[i].append([centroid, stu])
                        name = str(df.loc[df["ID"] == centroidID[i]]["Student Name"].iloc[0])
                        groups[(centroidID[i], name)][0] = g[i]
                        for t in totalMatches:
                            groups[(centroidID[i], name)][1][tuple(df.iloc[t].drop("ID").drop("Student Name"))].add((centroidID[i], name))
                            groups[(centroidID[i], name)][1][tuple(df.iloc[t].drop("ID").drop("Student Name"))].add((centroid, stu))

                        #Drop the centroid thats matched with another
                        groups.pop(centroid,None)
                        noCents = [x for x in students if x not in centroidID]

                        #Update centroidID
                        newChoice = randCentroids(noCents,1)
                        centroidID.remove(centroid)
                        centroidID.append(newChoice[0])
                        index = df.index
                        condition = df['ID'].isin(centroidID)
                        centroidInd = index[condition].tolist()
                        groups[centroidID[-1]] = []

                        #drop classes that matched
                        for ind in totalMatches:
                            if df.iloc[ind].ID == bestCluster:
                                #Find where it has a match with the centroid
                                cMatch = findMatch(df,ind,centroid,nameSim,locSim,timeSim,daySim)
                                #drop the appropriate index
                                tempDF.drop([cMatch],inplace = True)
                                tempDF = tempDF.reset_index(drop=True)

                            elif df.iloc[ind].ID == centroid:

                                tempDF.drop([ind],inplace=True)
                                tempDF = tempDF.reset_index(drop=True)

        #Create a list with the student IDS for all Non Centroid students
        notCentroids = [x for x in students if x not in centroidID]

        #Create a list with the indices for all centroid's classes
        index = df.index
        condition = df['ID'].isin(centroidID)
        centroidInd = index[condition].tolist()


        for student in notCentroids:
            #Create a list with the indices for all centroid's classes
            totalMatches = []
            #Counts dictionary will see how many similar classes a student has with a centroid
            counts = dict((el,0) for el in centroidID)

            #indexes for students classes
            index = df.index
            condition = df['ID'].isin([student])
            courses = index[condition].tolist()

            #get feature similarities
            nameSim = getSimMatrix(df['Class Name'])
            locSim = getSimMatrix(df['Class Loc'])
            timeSim = getSimMatrix(df['Class Time'])
            daySim = getSimMatrix(df['Days'])

            #for each course the student has
            for course in courses:


                matches = getMatches(df,course,nameSim,locSim,timeSim,daySim) #list of matches

                #check to see if the matches are centroids otherwise discard them
                for match in matches:
                    if match in centroidInd and df.iloc[match].ID != student:
                        cID = df.iloc[match].ID
                        totalMatches.append(match)
                        counts[cID] +=1

            bestCluster = max(counts,key=counts.get) #centroid the student has the most matches with

            #match it to the proper dictionary key
            for i in range(k):
                if bestCluster == centroidID[i] and len(g[i]) < 10 and counts[bestCluster] >= threshold:

                    #append to the appropriate group
                    stu = str(df.loc[df["ID"] == student]["Student Name"].iloc[0])
                    g[i].append([student, stu])
                    name = str(df.loc[df["ID"] == centroidID[i]]["Student Name"].iloc[0])
                    groups[(centroidID[i], name)][0] = g[i]
                    for t in totalMatches:
                        groups[(centroidID[i], name)][1][tuple(df.iloc[t].drop("ID").drop("Student Name"))].add((centroidID[i], name))
                        groups[(centroidID[i], name)][1][tuple(df.iloc[t].drop("ID").drop("Student Name"))].add((student, stu))

                    #drop the class so it doesn't get matched again
                    for ind in totalMatches:

                        if df.iloc[ind].ID == bestCluster:
                            cMatch = findMatch(df,ind,student,nameSim,locSim,timeSim,daySim) #the index the students class
                            otherInd = findEqivRow(df,tempDF,cMatch) #find the eqivalent index for the smaller dataframe
                            tempDF.drop(otherInd,inplace=True) #drop the class
                            tempDF = tempDF.reset_index(drop=True)
                        elif df.iloc[ind].ID == student:
                            tempDF.drop([ind],inplace=True) #drop if the index is correct
                            tempDF = tempDF.reset_index(drop=True)
                        else:
                            tempDF = tempDF.reset_index(drop=True) #reset index
        # if thredhold > 1 and there are at least 5 students left to be centroids, lower threshold and recurse
        if threshold > 1 and len(tempDF.ID.unique()) >= 5: 
            if count >= 5:
                threshold -=1
                helper(tempDF,5,threshold,0)
            else:
                count +=1
                helper(tempDF,5,threshold,count)
        # if threshold is 1 but there are still students to match, recurse without lowering threshold
        elif threshold == 1 and len(tempDF.ID.unique()) >= 5 :
            if count >= 5:
                #print("done matching\n")
                pass
            else:
                count +=1
                helper(tempDF,5,threshold,count)

        new_dict = dict((k,v) for k,v in groups.items() if v[0])
        match = {}
        
        if new_dict:
            for centroid in new_dict:
                matches, courses = new_dict[centroid]
                ml = list(matches)
                ml.extend([list(centroid)])
                MATCHES.append({"_id": {}, "matchingCourses": [], "students": []})
                MATCHES[-1]["_id"]["groupName"] = " ".join(em for em, sn in ml)
                print(centroid, matches)
                for c, students in courses.items():
                    cn, day, sem, sdt, loc = c
                    MATCHES[-1]["_id"]["semester"] = sem
                    keys = ["courseName", "day", "startDT", "loc", "studentsTaking"]
                    MATCHES[-1]["matchingCourses"].append(dict(zip(keys, [cn, day, sdt, loc, []])))
                    for em, sn in students:
                        MATCHES[-1]["matchingCourses"][-1]["studentsTaking"].append({"studentName":sn})
                    print(c, students)
                print()
                keys = ["email", "studentName"]
                
                for em, sn in ml:
                    MATCHES[-1]["students"].append(dict(zip(keys,[em,sn])))
    MATCHES = []
    
    helper(df,5,1,1)
    return MATCHES  

pd.set_option('display.max_rows', None)
myclient = pymongo.MongoClient("mongodb+srv://gm:tplySna2ZYaJbUif@cluster0.bbipje5.mongodb.net/?retryWrites=true&w=majority&ssl=true&ssl_cert_reqs=CERT_NONE&tls=true")
db = myclient["groupMatch"]
df = pd.DataFrame()
takesCol = db.takes
ls = list(takesCol.find())
for s in ls:
    for c in s["courses"]:
        dt, days, loc, cn = c["startDT"], c["days"], c["loc"], c["courseName"]
        if cn != "EXAMINATION HOUR":
            df = df.append({"ID": s["_id"]["email"], "Student Name": s["studentName"],"Class Name": cn, "Days": days, "Sem": s["_id"]["semester"], "Class Time": dt, "Class Loc": loc}, ignore_index=True)
print(df)

dfNames = df['Class Name']
dfLocs = df['Class Loc']
dfTimes = df['Class Time']
dfDays = df['Days']

#TF-IDF the Names First
tfIdfVectorizer = TfidfVectorizer()
XNames = tfIdfVectorizer.fit_transform(dfNames)

'''
.indices on sparse matrix for indexes of all non 0 matches
index with [og row, other row]
Ex.) row 5 is being checked want to get the values?
matrix.indices gives the matching row indexes: [67 43 28 24 11  5]
then to get the values from the specific row matches:
matrix[5,67] -> 1.0
matrix[5,43] -> .4102
'''
similarities = cosine_similarity(XNames)
similarities_sparse = cosine_similarity(XNames,dense_output=False)
ind = similarities_sparse[14].indices

tfIdfVectorizerLoc = TfidfVectorizer()
XLoc = tfIdfVectorizerLoc.fit_transform(dfLocs)
similarities = cosine_similarity(XLoc)
similarities_sparse = cosine_similarity(XLoc,dense_output=False)

tfIdfVectorizerTimes = TfidfVectorizer()
XTimes = tfIdfVectorizerTimes.fit_transform(dfTimes)
similarities = cosine_similarity(XTimes)
similarities_sparse = cosine_similarity(XTimes,dense_output=False)

tfIdfVectorizerDays = TfidfVectorizer()
XDays = tfIdfVectorizerDays.fit_transform(dfDays)
similarities = cosine_similarity(XDays)
similarities_sparse = cosine_similarity(XDays,dense_output=False)

allGroups = matchAll(df,5,1,1)
groupsCol = db["groups"]
for g in allGroups:
    q = { "groupName": g["_id"]["groupName"] }
    if db.mycollection.count_documents(q) == 0:
        try: 
            insIds = groupsCol.insert_one(g)
            print(insIds.inserted_id)
        except:
            print(g["_id"]["groupName"], " exists already")
    else:
        print(g["_id"]["groupName"], " exists already")
