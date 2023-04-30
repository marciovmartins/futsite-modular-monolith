import React, {useEffect, useState} from "react";

export function AmateurSoccerGroupList(
    {setViewLink, setCreationLink}
) {
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
            {amateurSoccerGroups.map(amateurSoccerGroup => {
                const selfLink = amateurSoccerGroup._links.self.href
                const name = amateurSoccerGroup.name

                return <li key={selfLink}>
                    {name}{' '}
                    <button onClick={() => setViewLink(selfLink)}>View</button>
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