import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import UploadCal from "./components/UploadCal";
import Home from "./components/Home";
import StudentHome from "./components/StudentHome";
import AdminHome from "./components/AdminHome";
import LoginAdmin from "./components/LoginAdmin";
import LoginStudent from "./components/LoginStudent";
import RegisterAdmin from "./components/RegisterAdmin";
import RegisterStudent from "./components/RegisterStudent";
import ViewCourses from "./components/ViewCourses";
import ViewGroups from "./components/ViewGroups";
import SearchGroup from "./components/SearchGroup";
import SearchCourse from "./components/SearchCourse";
import SearchStudent from "./components/SearchStudent";
import AddGroup from "./components/AddGroup";
import UpdateGroup from "./components/UpdateGroup";
 
function App() {
  return (
    <Router>
    <div className="container">
      <div className="columns">
        <div className="column is-half is-offset-one-quarter">
          <Routes>
            <Route exact path="/" element={<Home />}></Route>
            <Route exact path="/uploadCal" element={<UploadCal />}></Route>
            <Route exact path="/studentHome" element={<StudentHome />}></Route>
            <Route exact path="/adminHome" element={<AdminHome />}></Route>
            <Route exact path="/registerAdmin" element={<RegisterAdmin />}></Route>
            <Route exact path="/registerStudent" element={<RegisterStudent />}></Route>
            <Route exact path="/loginAdmin" element={<LoginAdmin />}></Route>
            <Route exact path="/loginStudent" element={<LoginStudent />}></Route>
            <Route exact path="/viewCourses" element={<ViewCourses />}></Route>
            <Route exact path="/viewGroups" element={<ViewGroups />}></Route>
            <Route exact path="/searchGroup" element={<SearchGroup />}></Route>
            <Route exact path="/searchCourse" element={<SearchCourse />}></Route>
            <Route exact path="/searchStudent" element={<SearchStudent />}></Route>
            <Route exact path="/addGroup" element={<AddGroup />}></Route>
            <Route exact path="/updateGroup" element={<UpdateGroup />}></Route>

          </Routes>
        </div>
      </div>
    </div>
    </Router>
  );
}

export default App;

