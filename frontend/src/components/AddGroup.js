import { useState, useEffect } from 'react'
import axios from "axios";
import { Link } from "react-router-dom";
import moment from 'moment';
import Cookies from 'js-cookie';
import { JsonToTable } from "react-json-to-table";

const AddGroup = () => {
    useEffect(() => {
        checkUser();
    }, []);

    const checkUser = async() => {
        if (Cookies.get("type") != "admin") {
            alert("access denied");
            window.location.assign("/");
        }
    }
 
    return (
        <div className = "main">
            <Link to="/">Home</Link><br/>
        </div>
    )
}

export default AddGroup