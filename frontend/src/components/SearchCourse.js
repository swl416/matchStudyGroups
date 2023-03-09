import { useState, useEffect } from 'react'
import axios from "axios";
import { Link } from "react-router-dom";
import moment from 'moment';
import Cookies from 'js-cookie';
import { JsonToTable } from "react-json-to-table";

const SearchCourse = () => {
    const [courseName, setCourseName] = useInput('');
    const [startDt, setStartDt] = useInput('');
    const [days, setDays] = useInput('');
    const [loc, setLoc] = useInput('');
    const [students, setStudents] = useState([]);

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
        const resp = axios.get(`http://localhost:8080/admin/searchCourse/${courseName}/${startDt}/${days}/${loc}`)
        .then( (resp) => {
            if (resp.data) {
                console.log(resp.data);
                setStudents(resp.data)
            } else {
                alert("group not found");
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
                <input placeholder="Course Name" value={courseName} onChange={setCourseName}/>
                <input placeholder="Start DateTime" value={startDt} onChange={setStartDt}/>
                <input placeholder="Days" value={days} onChange={setDays}/>
                <input placeholder="Location" value={loc} onChange={setLoc}/>
                <button>Submit</button>
            </form>
            <JsonToTable json = {students}></JsonToTable>
        </div>
    )
}

export default SearchCourse