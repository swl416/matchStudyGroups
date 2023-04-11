import { useState, useEffect } from 'react'
import axios from "axios";
import { Link } from "react-router-dom";
import Cookies from 'js-cookie';

const DeleteGroup = () => {

    const [sem,setSem] = useInput('');
    const [groupName,setGroup] = useInput('');

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
        const resp = axios.delete(`http://localhost:8080/admin/delGroup/${groupName}/${sem}`)
        .then( (resp) => {
            if (resp.data) {
                console.log(resp);
                alert(resp.data);
            } else {
                alert("group does not exist");
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
            <h2>Delete Group</h2>
            <form onSubmit={handleSubmit}>
                <input placeholder="Group Name"
                value={groupName} onChange={setGroup}/>
                <input placeholder="Semester"
                value={sem} onChange={setSem}/>
                <button>Submit</button>
            </form>
        </div>
        
    )
}

export default DeleteGroup