import { useState, useEffect } from 'react'
import axios from "axios";
import { Link } from "react-router-dom";
import moment from 'moment';
import Cookies from 'js-cookie';
import { JsonToTable } from "react-json-to-table";

const RegisterAdmin = () => {

    const [user,setUser] = useInput('');
    const [password,setPassword] = useInput('');
    const [name,setName] = useInput('');

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
        const resp = axios.post(`http://localhost:8080/admin/register/${user}/${name}/${password}`)
        .then( (resp) => {
            if (resp.data) {
                console.log(resp);
                alert(resp.data);
                window.location.assign("/loginAdmin");
            } else {
                alert("invalid input");
            }
            
        })
        .catch( (error) => {
            console.log(error.response.data);
            alert(error.response.data);
        });
    }

    return(
        <div className = "main">
            <Link to="/" className="button is-primary mt-2">Home</Link>
            <h2>Register Admin</h2>
            <form onSubmit={handleSubmit}>
                <input placeholder="Username" type="user"
                value={user} onChange={setUser}/>
                <input placeholder="Password" type="password"
                value={password} onChange={setPassword}/>
                <input placeholder="Name" type="name"
                value={name} onChange={setName}/>
                <button>Submit</button>
            </form>
        </div>
        
    )
}

export default RegisterAdmin