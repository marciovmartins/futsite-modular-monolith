import React, {useEffect, useState} from "react";

export function AmateurSoccerGroupList(
    {setViewUrl, setCreationUrl}
) {
    const [amateurSoccerGroups, setAmateurSoccerGroups] = useState([]);

    useEffect(() => {
        fetchAmateurSoccerGroups().then((list) => {
            setAmateurSoccerGroups(list._embedded.amateurSoccerGroups)
            setCreationUrl(list._links?.create?.href)
        })
    }, [])

    return <div>
        <h1>Amateur Soccer Groups</h1>
        <ul>
            {amateurSoccerGroups.map((amateurSoccerGroup, amateurSoccerGroupIndex) => {
                return <li key={amateurSoccerGroupIndex}>
                    {amateurSoccerGroup.name}{' '}
                    <button onClick={() => {
                        setViewUrl(amateurSoccerGroup._links.self.href)
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