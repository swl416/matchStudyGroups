import { useState, useEffect } from 'react'
import axios from "axios";
import { Link } from "react-router-dom";
import moment from 'moment';
import Cookies from 'js-cookie';
import { JsonToTable } from "react-json-to-table";

const LoginStudent = () => {

    const [email,setEmail] = useInput('');
    //const [school,setSchool] = useInput('');
    const [password,setPassword] = useInput('');

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
        if (Cookies.get("type") == "student") {
            window.location.assign("/studentHome");
        } else if (Cookies.get("type") == "admin") {
            window.location.assign("/adminHome");
        }
    }

    function handleSubmit(event){
        event.preventDefault();
        const resp = axios.get(`http://localhost:8080/student/auth/${email}/${password}`)
        .then(function (resp) {
            console.log(resp)
            console.log(resp.data);
            if (resp.data == true) {
                Cookies.set('type', 'student');
                Cookies.set("email",email)
                //Cookies.set("school",school)
                alert("Login successful");
                window.location.assign("/");
            } else {
                alert("invalid email or password");
            }
            
        })
        .catch(function (error) {
            console.log(error.response.data);
            alert(error.response.data);
        });
    }

    return(
        <div className = "main">
            <Link to="/" className="button is-primary mt-2">Home</Link>
            <h2>Student Login</h2>
            <form onSubmit={handleSubmit}>
                {/* <input placeholder="School" type="school"
                value={school} onChange={setSchool}/> */}
                <input placeholder="Email" type="email"
                value={email} onChange={setEmail}/>
                <input placeholder="Password" type="password"
                value={password} onChange={setPassword}/>
                <button>Submit</button>
            </form>
        </div>
        
    )
}

export default LoginStudent