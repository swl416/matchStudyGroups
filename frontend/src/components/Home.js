import { useState, useEffect } from 'react'
//import axios from "axios";
import { Link } from "react-router-dom";
import Cookies from 'js-cookie';


const Home = () => {
    useEffect(() => {
        checkUser();
    }, []);
    const checkUser = async() => {
        if (Cookies.get("type") == "student") {
            window.location.assign("/studentHome");
        } else if (Cookies.get("type") == "admin") {
            window.location.assign("/adminHome");
        }
    }
    
    return (
        <div className = "main">
            <Link to="/loginStudent" className="button is-primary mt-2">Login as Student</Link>
            <br/>
            <Link to="/loginAdmin" className="button is-primary mt-2">Login as Admin</Link>
            <br/>
            <Link to="/registerStudent" className="button is-primary mt-2">Register as Student</Link>
            <br/>
            <Link to="/registerAdmin" className="button is-primary mt-2">Register as Admin</Link>


        </div>
    )
}

export default Home