import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";

export function GamedayList(
    {uri, setCreationLink, creationLink}
) {
    const [gamedays, setGamedays] = useState([])

    useEffect(() => {
        fetchGamedays(uri).then(data => {
            setCreationLink(data._links?.["create-gameday"]?.href)
        })
    }, [gamedays])

    return <div>
        <h1>Gamedays</h1>

        {gamedays.length === 0 && <p>
            No game days registered.
            {creationLink
                && <span> Click <Link to="/gamedays/new" state={{gamedaysLink: uri}}>here</Link> to register one</span>}
        </p>}
    </div>
}

function fetchGamedays(uri) {
    return fetch(uri, {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    }).then(response => response.json())
}