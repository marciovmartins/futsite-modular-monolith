import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";

export function AmateurSoccerGroupList(
    {setViewUrl, setCreationUrl, setUserDataCreationUrl, urlToNewAmateurSoccerGroup}
) {
    const [amateurSoccerGroups, setAmateurSoccerGroups] = useState([]);

    useEffect(() => {
        fetchAmateurSoccerGroups().then((list) => {
            setCreationUrl(list._links?.create?.href)
            setUserDataCreationUrl(list._links?.["create-user-data"]?.href)
            Promise.all(list._embedded.amateurSoccerGroups.map(it => fetchAmateurSoccerGroupUserData(it._links["get-user-data"].href)))
                .then(values => {
                    setAmateurSoccerGroups(values.map((userData, index) => ({
                        name: userData.name,
                        url: list._embedded.amateurSoccerGroups[index]._links.self.href
                    })))
                })
        })
    }, [])

    return <div>
        <h1>
            Amateur Soccer Groups
            {urlToNewAmateurSoccerGroup &&
                <Link to={urlToNewAmateurSoccerGroup} className="btn btn-primary">New</Link>}
        </h1>
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