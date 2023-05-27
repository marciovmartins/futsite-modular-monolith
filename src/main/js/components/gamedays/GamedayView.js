import React, {useEffect, useState} from "react";

export function GamedayView(
    {url}
) {
    const [gameday, setGameday] = useState({
        date: '',
        matches: []
    })

    const gamedayUrl = url || window.sessionStorage.getItem("gamedayUrl")

    useEffect(() => window.sessionStorage.setItem("gamedayUrl", gamedayUrl), [url])

    useEffect(() => {
        fetchUrl(gamedayUrl).then(gameday => {
            const playerIds = gameday.matches.flatMap((match) => match.players.map(playerStatistic => playerStatistic.playerId))
            Promise.all(playerIds.map((playerId) => fetchUrl(gameday._links["get-player-user-data-" + playerId].href))).then(values => {
                gameday.matches.forEach((match) => {
                    match.players.forEach((playerStatistic) => {
                        const player = values.find((it) => it._links.self.href.includes(playerStatistic.playerId))
                        playerStatistic.playerName = player.name
                    })
                })
                setGameday(gameday)
            })
        })
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
                            <td>{player.playerName}</td>
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

function fetchUrl(link) {
    return fetch(link, {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    })
        .then(response => response.json())
}