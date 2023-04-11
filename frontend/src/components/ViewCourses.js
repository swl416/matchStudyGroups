import { useState, useEffect } from 'react'
import axios from "axios";
import { Link } from "react-router-dom";
import moment from 'moment';
import Cookies from 'js-cookie';
import { JsonToTable } from "react-json-to-table";

const ViewGroups = () => {

    const [courses, setCourses] = useState([]);

    useEffect(() => {
        checkUser();
        viewGroups();
    }, []);
 

    const checkUser = async() => {
        if (Cookies.get("type") != "student") {
            alert("access denied");
            window.location.assign("/");
        }
    }

    const viewGroups = async() => {
        const resp = axios.get(`http://localhost:8080/student/viewCourses/${Cookies.get("email")}`)
        .then(function (resp) {
            console.log(resp.data);
            if (resp.data) {
                setCourses(resp.data);
            } else {
                alert("no courses");
            }
            
        })
        .catch(function (error) {
            console.log(error.response.data);
            alert(error.response.data);
        });
    }

 
    return (
        <div className = "main">
            <Link to="/">Home</Link><br/>
            <h2>View Courses</h2>
            <JsonToTable json = {courses}></JsonToTable>
        </div>
    )
}

export default ViewGroups