import { useState, useEffect } from 'react'
import axios from "axios";
import { Link } from "react-router-dom";
import moment from 'moment';
import Cookies from 'js-cookie';
import { JsonToTable } from "react-json-to-table";

const SearchStudent = () => {
    const [email, setEmail] = useInput('');
    const [group, setGroup] = useState([]);
    const [courses, setCourses] = useState([]);

    function useInput(initialValue){
        const [value,setValue] = useState(initialValue);
     
         function handleChange(e){
             setValue(e.target.value);
         }
     
        return [value,handleChange];
    }

    useEffect(() => {
        checkUser();
    }, []);

    const checkUser = async() => {
        if (Cookies.get("type") != "admin") {
            alert("access denied");
            window.location.assign("/");
        }
    }

    function handleSubmit(event){
        event.preventDefault();
        const res = axios.get(`http://localhost:8080/student/viewCourses/${email}`)
        .then( (res) => {
            if (res.data) {
                console.log(res.data);
                setCourses(res.data)
            } else {
                alert("student or courses not found");
            }
            
        })
        .catch( (error) => {
            console.log(error.response.data);
            alert(error.response.data);
        });
        const resp = axios.get(`http://localhost:8080/student/viewGroups/${email}`)
        .then( (resp) => {
            if (resp.data) {
                console.log(resp.data);
                setGroup(resp.data)
            } else {
                alert("student or groups not found");
            }
            
        })
        .catch( (error) => {
            console.log(error.response.data);
            alert(error.response.data);
        });
    }
 
    return (
        <div className = "main">
            <Link to="/">Home</Link><br/>
            <form onSubmit={handleSubmit}>
                <input placeholder="Student Email" value={email} onChange={setEmail}/>
                <button>Submit</button>
            </form>
            <JsonToTable json = {courses}></JsonToTable>
            <JsonToTable json = {group}></JsonToTable>
        </div>
    )
}

export default SearchStudent