import { useState, useEffect } from 'react'
import axios from "axios";
import { Link } from "react-router-dom";
import moment from 'moment';
import Cookies from 'js-cookie';
import { JsonToTable } from "react-json-to-table";

const RegisterStudent = () => {

    const [email,setEmail] = useInput('');
    const [password,setPassword] = useInput('');
    const [name,setName] = useInput('');
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
        if (Cookies.get("type") == "student") {
            window.location.assign("/studentHome");
        } else if (Cookies.get("type") == "admin") {
            window.location.assign("/adminHome");
        }
    }

    function handleSubmit(event){
        event.preventDefault();
        const resp = axios.post(`http://localhost:8080/student/register/${email}/${name}/${password}`)
        .then( (resp) => {
            if (resp.data) {
                console.log(resp);
                alert(resp.data);
                window.location.assign("/loginStudent");
            } else {
                alert("invalid input");
            }
            
        })
        .catch( (error) => {
            console.log(error.response.data);
            alert(error.response.data);
        });
        // const resp = axios.get(`http://localhost:3000/student/authStudent/${Cookies.get("email")}`)
        // .then((resp) => {
        //     if (!resp.data.exists) {
        //         const response = axios.post(`http://localhost:3000/student`,{
        //             email: email,
        //             pw: password,
        //             name: name,
        //             //school: school
        //         })
        //         .then( (response) => {
        //             if (response.data) {
        //                 alert(response.data.msg);
        //                 window.location.assign("/loginStudent");
        //             } else {
        //                 alert("invalid input");
        //             }
                    
        //         })
        //         .catch( (error) => {
        //             console.log(error);
        //             alert("invalid input");
        //         });
        //     } else {
        //         alert("student already exists with this email");
        //     }
        // })
        // .catch((err) => {
        //     console.log(err);
        //     alert("invalid input");
        // });
    }

    return(
        <div className = "main">
            <Link to="/" className="button is-primary mt-2">Home</Link>
            <h2>Register Student</h2>
            <form onSubmit={handleSubmit}>
                <input placeholder="Email" type="email"
                value={email} onChange={setEmail}/>
                <input placeholder="Password" type="password"
                value={password} onChange={setPassword}/>
                <input placeholder="Name" type="name"
                value={name} onChange={setName}/>
                {/* <input placeholder="School" type="school"
                value={school} onChange={setSchool}/> */}
                <button>Submit</button>
            </form>
        </div>
        
    )
}

export default RegisterStudent