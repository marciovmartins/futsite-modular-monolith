import React, {useEffect, useState} from "react";

export function GamedayView(
    {uri}
) {
    const [gameday, setGameday] = useState({
        date: '',
        matches: []
    })

    useEffect(() => {
        fetchGameday(uri).then(setGameday)
    }, [])

    return <div>
        <h1>Gameday</h1>

        Date: {gameday.date}

        <h2>Matches</h2>

        {gameday.matches.map((match, index) => {
            return <div key={index}>
                <b>#{index + 1}</b>

                <table>
                    <thead>
                    <tr>
                        <th>Player</th>
                        <th>Team</th>
                        <th>Goals in Favor</th>
                        <th>Own Goals</th>
                        <th>Yellow Cards</th>
                        <th>Blue Cards</th>
                        <th>Red Cards</th>
                    </tr>
                    </thead>
                    <tbody>
                    {match.players.map((player, playerIndex) => {
                        return <tr key={playerIndex}>
                            <td>{player.playerId}</td>
                            <td>{player.team}</td>
                            <td>{player.goalsInFavor}</td>
                            <td>{player.ownGoals}</td>
                            <td>{player.yellowCards}</td>
                            <td>{player.blueCards}</td>
                            <td>{player.redCards}</td>
                        </tr>
                    })}
                    </tbody>
                </table>
            </div>
        })}
    </div>
}

function fetchGameday(link) {
    return fetch(link, {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    })
        .then(response => response.json())
}