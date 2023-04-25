import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";

export function AmateurSoccerGroupList() {
    const [state, setState] = useState({amateurSoccerGroups: []});

    useEffect(() => loadList(setState), []);

    return <div>
        <h1>Amateur Soccer Group List</h1>
        <ul>
            {state.amateurSoccerGroups.map(amateurSoccerGroup => {
                const selfLink = amateurSoccerGroup._links.self.href
                const name = amateurSoccerGroup.name
                return <li key={selfLink}>
                    <Link to={selfLink}>{name} - CHANGED (3.7)</Link>
                </li>
            })}
        </ul>
    </div>;
}

function loadList(setState) {
    fetch("http://localhost:8080/api/amateurSoccerGroups", {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    })
        .then(response => response.json())
        .then(data => data._embedded.amateurSoccerGroups)
        .then(amateurSoccerGroups => setState({amateurSoccerGroups}))
}