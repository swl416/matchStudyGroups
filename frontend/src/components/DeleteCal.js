import { useState, useEffect } from 'react'
import axios from "axios";
import { Link } from "react-router-dom";
import Cookies from 'js-cookie';

const DeleteCal = () => {

    const [email,setEmail] = useInput('');
    const [sem,setSem] = useInput('');
    //const [school,setSchool] = useInput('');

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
        if (Cookies.get("type") != "student") {
            alert("access denied");
            window.location.assign("/");
        }
    }

    function handleSubmit(event){
        event.preventDefault();
        const resp = axios.delete(`http://localhost:8080/student/delCal/${Cookies.get("email")}/${sem}`)
        .then(function (resp) {
            console.log(resp.data);
            if (resp.data) {
                alert(resp.data);
            } else {
                alert("courses could not be deleted. try again");
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
            <h2>Delete Course Schedule</h2>
            <form onSubmit={handleSubmit}>
                <input placeholder="Semester"
                    value={sem} onChange={setSem}/>
                <button>Submit</button>
            </form>
        </div>
    )
}

export default DeleteCal