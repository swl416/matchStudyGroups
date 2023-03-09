import { useState, useEffect } from 'react'
import axios from "axios";
import { Link } from "react-router-dom";
import moment from 'moment';
import Cookies from 'js-cookie';
import { JsonToTable } from "react-json-to-table";

const SearchGroup = () => {

    const [groupName, setGroupName] = useInput('');
    const [sem, setSem] = useInput('');
    const [group, setGroup] = useState([]);

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
        const resp = axios.get(`http://localhost:8080/admin/searchGroup/${groupName}/${sem}`)
        .then( (resp) => {
            if (resp.data) {
                console.log(resp.data);
                setGroup(resp.data)
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
                <input placeholder="Group Name" value={groupName} onChange={setGroupName}/>
                <input placeholder="Semester" value={sem} onChange={setSem}/>
                <button>Submit</button>
            </form>
            <JsonToTable json = {group}></JsonToTable>
        </div>
    )
}

export default SearchGroup