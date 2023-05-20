import React, {useEffect, useState} from "react";

export function AmateurSoccerGroupList(
    {setViewUrl, setCreationUrl}
) {
    const [amateurSoccerGroups, setAmateurSoccerGroups] = useState([]);

    useEffect(() => {
        fetchAmateurSoccerGroups().then((list) => {
            Promise.all(list._embedded.amateurSoccerGroups.map(it => fetchAmateurSoccerGroupUserData(it._links["get-user-data"].href)))
                .then(values => {
                    setAmateurSoccerGroups(values.map((userData, index) => ({
                        name: userData.name,
                        url: list._embedded.amateurSoccerGroups[index]._links.self.href
                    })))
                    setCreationUrl(list._links?.create?.href)
                })
        })
    }, [])

    return <div>
        <h1>Amateur Soccer Groups</h1>
        <ul>
            {amateurSoccerGroups.map((amateurSoccerGroup, amateurSoccerGroupIndex) => {
                return <li key={amateurSoccerGroupIndex}>
                    {amateurSoccerGroup.name}{' '}
                    <button onClick={() => {
                        setViewUrl(amateurSoccerGroup.url)
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

function fetchAmateurSoccerGroupUserData(url) {
    return fetch(url, {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    }).then(response => response.json())
}