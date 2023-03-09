const fs = require('fs');
const moment = require('moment');

var total = [];
const info = fs.readFileSync("1.ics", 'UTF-8');
const lines = info.split(/\r?\n/);
var i = 0;
var attributes = new Set();

attributes.add('SUMMARY');
attributes.add('DTSTART');
attributes.add('LOCATION');
attributes.add('RRULE');
//var h = {'SUMMARY':"courseName", "DTSTART":"startDT", "LOCATION":"loc", "RRULE":"day"};
const numFeatures = 4;
var curr = [];
var beg = false;
while (i < lines.length) {
    if (lines[i] == "BEGIN:VEVENT") {
        beg = true;
    }
    ls = lines[i].split(':').join('*').split(';').join('*').split('*')
    //console.log(ls)
    if (ls.length >= 2 && attributes.has(ls[0]) && beg) {
        if (ls[0] == "RRULE") {
            curr.push(ls[ls.length-1].split("=")[1]);
        } else if (ls[0] == "DTSTART") {
            var dt = ls[ls.length-1];
            dt = dt.substring(0,4) + "-" + dt.substring(4,6) + "-" + dt.substring(6,11) + ":" + dt.substring(11,13) + ":" + dt.substring(13,15);
            dt = moment(dt).format("YYYY-MM-DD HH:mm:ss");
            curr.push(dt);
        }
        else {
            curr.push(ls.slice(1,ls.length).join(" "));
        }
    }
    if (curr.length == numFeatures) {
        total.push(curr);
        curr = [];
        beg == false;
    }
    
    i++;
}

console.log(total)