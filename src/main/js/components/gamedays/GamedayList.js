import React, {useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";

export function GamedayList(
    {uri, setCreationLink, creationLink, setViewLink, setAmateurSoccerGroupLink}
) {
    const navigate = useNavigate()
    const [gamedays, setGamedays] = useState([])

    useEffect(() => {
        fetchGamedays(uri).then(data => {
            setCreationLink(data._links?.["create-gameday"]?.href)
            setAmateurSoccerGroupLink(data._links?.["get-amateur-soccer-group"]?.href)
            let sortedGamedays = data._embedded?.gamedays?.sort((a, b) => (a.date > b.date) - (a.date < b.date)) || []
            setGamedays(sortedGamedays)
        })
    }, [])

    return <div>
        <h1>Gamedays</h1>

        {gamedays.length === 0 && <p>
            No game days registered.
            {creationLink
                && <span> Click <Link to="/gamedays/new" state={{gamedaysLink: uri}}>here</Link> to register one</span>}
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
                        <button onClick={() => {
                            setViewLink(selfLink)
                            navigate("/gamedays/view")
                        }}>{gameday.date}</button>
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