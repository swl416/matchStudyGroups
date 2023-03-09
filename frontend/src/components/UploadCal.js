import { useState, useEffect } from 'react'
import axios from "axios";
import { Link } from "react-router-dom";
import Cookies from 'js-cookie';
//import { JsonToTable } from "react-json-to-table";

const UploadCal = () => {
    // const [school,setSchool] = useInput('');

    // function useInput(initialValue){
    //     const [value,setValue] = useState(initialValue);
     
    //      function handleChange(e){
    //          setValue(e.target.value);
    //      }
     
    //     return [value,handleChange];
    // }
    
    useEffect(() => {
        checkUser();
    }, []);
 
    const handleSubmit = async (event) => {
        event.preventDefault();
        const file = document.getElementById("cal").files[0];
        const formData = new FormData();
        formData.append('file', file);

        //const res = await axios.post(`http://localhost:8080/cal/parseCal?school=${school}&email=${Cookies.get("email")}`, formData, {
        const res = await axios.post(`http://localhost:8080/student/uploadCal?email=${Cookies.get("email")}`, formData, {
            // school: school,
            // email: Cookies.get("email"),
            headers: {
                "Content-Type": "multipart/form-data"
            },
        })
        .then( function (res) {
            console.log(res.data);
            if (res.data) {
                alert("successfully uploaded!");
                //window.location.assign("/");
            } else {
                alert("invalid input");
            }
            
        })
        .catch(function (error) {
            console.log(error);
            alert("invalid input");
        });
         
    }
    
    const checkUser = async() => {
        if (Cookies.get("type") != "student") {
            alert("access denied");
            window.location.assign("/");
        }
    }
    

    /*
    <input type="checkbox" id="major" name="major" value="major"/>
    <label for="major">Major</label><br/>
    <input type="checkbox" id="gradDate" name="gradDate" value="gradDate"/>
    <label for="gradDate">Graduation Date</label><br/>
    */

    return (
        <div className = "main">
            <Link to="/">Home</Link><br/>
            <h2>Upload Calendar</h2>
            <form onSubmit={handleSubmit}>
                {/* <input placeholder="School" type = "school" value={school} onChange={setSchool}/><br/> */}
                <input id = "cal" type="file" /><br/>
                <input type="submit" value="Submit" />
            </form>
            
            
        </div>
    )
}

export default UploadCal