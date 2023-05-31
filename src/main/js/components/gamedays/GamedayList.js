import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";

export function GamedayList(
    {url, setCreationUrl, creationUrl, setViewUrl, setAmateurSoccerGroupUrl}
) {
    const [gamedays, setGamedays] = useState([])

    useEffect(() => {
        fetchGamedays(url).then(data => {
            setCreationUrl(data._links?.["create-gameday"]?.href)
            setAmateurSoccerGroupUrl(data._links?.["get-amateur-soccer-group"]?.href)
            let sortedGamedays = data._embedded?.gamedays?.sort((a, b) => (a.date > b.date) - (a.date < b.date)) || []
            setGamedays(sortedGamedays)
        })
    }, [])

    return <div>
        <h1>Gamedays</h1>

        {gamedays.length === 0 && <p>
            No game days registered.
            {creationUrl
                && <span> Click <Link to="/gamedays/new" state={{gamedaysLink: url}}>here</Link> to register one</span>}
        </p>}
        {gamedays.length > 0 && <table className="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Date</th>
                <th scope="col">Matches</th>
            </tr>
            </thead>
            <tbody>
            {gamedays.map((gameday, index) => {
                const selfLink = gameday._links.self.href
                return <tr key={index}>
                    <th scope="row">{index + 1}</th>
                    <td>
                        <button onClick={() => setViewUrl(selfLink)}>{gameday.date}</button>
                    </td>
                    <td>{gameday.matches.length}</td>
                </tr>
            })}
            </tbody>
        </table>}
    </div>
}

function fetchGamedays(uri) {
    return fetch(uri, {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    }).then(response => response.json())
}