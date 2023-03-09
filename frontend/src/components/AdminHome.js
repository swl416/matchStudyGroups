import { useState, useEffect } from 'react'
import axios from "axios";
import { Link } from "react-router-dom";
import moment from 'moment';
import Cookies from 'js-cookie';
import { JsonToTable } from "react-json-to-table";

const AdminHome = () => {
    useEffect(() => {
        checkUser();
    }, []);
 

    const checkUser = async() => {
        if (Cookies.get("type") != "admin") {
            alert("access denied");
            window.location.assign("/");
        }
    }

    const logOut = async() => {
        Cookies.remove("type");
        Cookies.remove("user");
        alert("successfully logged out");
        window.location.assign("/");
    }

    
 
    return (
        <div className = "main">
            <button onClick={logOut}>Log Out</button>
            <br/>
            <Link to="/searchGroup" >Search Group</Link>
            <br/>
            <Link to="/searchStudent" >Search Student</Link>
            <br/>
            <Link to="/searchCourse" >Search Course</Link>
            <br/>
            <Link to="/addGroup" >Add Group</Link>
            <br/>
            <Link to="/updateGroup" >Update Group</Link>

        </div>
    )
}

export default AdminHome