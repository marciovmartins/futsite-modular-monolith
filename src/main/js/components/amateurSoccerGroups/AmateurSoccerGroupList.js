import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

export function AmateurSoccerGroupList(
    {setViewLink, setCreationLink}
) {
    const navigate = useNavigate()
    const [amateurSoccerGroups, setAmateurSoccerGroups] = useState([]);

    useEffect(() => {
        fetchAmateurSoccerGroups().then((list) => {
            setAmateurSoccerGroups(list._embedded.amateurSoccerGroups)
            setCreationLink(list._links?.create?.href)
        })
    }, [])

    return <div>
        <h1>Amateur Soccer Groups</h1>
        <ul>
            {amateurSoccerGroups.map((amateurSoccerGroup, amateurSoccerGroupIndex) => {
                return <li key={amateurSoccerGroupIndex}>
                    {amateurSoccerGroup.name}{' '}
                    <button onClick={() => {
                        setViewLink(amateurSoccerGroup._links.self.href)
                        navigate("/amateurSoccerGroups/view")
                    }}>
                        View
                    </button>
                </li>
            })}
        </ul>
    </div>;
}

function fetchAmateurSoccerGroups() {
    return fetch("http://localhost:8080/api/amateurSoccerGroups", {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    }).then(response => response.json())
}