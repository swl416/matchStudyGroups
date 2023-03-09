import { useState, useEffect } from 'react'
import axios from "axios";
import { Link } from "react-router-dom";
import moment from 'moment';
import Cookies from 'js-cookie';
import { JsonToTable } from "react-json-to-table";

const StudentHome = () => {
    useEffect(() => {
        checkUser();
    }, []);
 

    const checkUser = async() => {
        if (Cookies.get("type") != "student") {
            alert("access denied");
            window.location.assign("/");
        }
    }

    const logOut = async() => {
        Cookies.remove("type");
        Cookies.remove("email");
        alert("successfully logged out");
        window.location.assign("/");
    }

    
 
    return (
        <div className = "main">
            <button onClick={logOut}>Log Out</button><br/>
            <Link to="/uploadCal">Upload Calendar</Link><br/>
            <Link to="/viewCourses">View Courses</Link><br/>
            <Link to="/viewGroups">View Groups</Link><br/>

        </div>
    )
}

export default StudentHome