import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";

export function GamedayList(
    {url, setCreationUrl, creationUrl, setViewUrl, setAmateurSoccerGroupUrl, urlToNewGameday}
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
        <h1>
            Gamedays
            {urlToNewGameday
                && <Link to={urlToNewGameday} className="btn btn-primary">New</Link>}
        </h1>

        {gamedays.length === 0 && <p>
            No game days registered.
            {creationUrl
                && <span> Click <Link to="/gamedays/new" state={{gamedaysLink: url}}>here</Link> to register one</span>}
        </p>}
        {gamedays.length > 0 && <table>
            <thead>
            <tr>
                <th>#</th>
                <th>Date</th>
                <th>Matches</th>
            </tr>
            </thead>
            <tbody>
            {gamedays.map((gameday, index) => {
                const selfLink = gameday._links.self.href
                return <tr key={index}>
                    <td>{index + 1}</td>
                    <td>
                        <button onClick={() => setViewUrl(selfLink)}>{gameday.date}</button>
                    </td>
                    <td align="right">{gameday.matches.length}</td>
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