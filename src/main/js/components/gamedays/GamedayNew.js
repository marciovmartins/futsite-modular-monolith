import React, {useEffect, useState} from "react";
import {v4 as uuidv4} from "uuid";
import {fetchUrl} from "../../api/fetchUrl";

export function GamedayNew(
    {creationUrl, setViewUrl, amateurSoccerGroupUrl}
) {
    const [formData, setFormData] = useState({
        date: '',
        matches: [],
    })

    const [players, setPlayers] = useState([])

    useEffect(() => {
        fetchUrl(amateurSoccerGroupUrl).then(amateurSoccerGroup => {
            fetchUrl(amateurSoccerGroup._links["get-players"].href).then(players => {
                Promise.all(players._embedded.players.map(player => fetchUrl(player._links["get-player-user-data"].href))).then(values => {
                    let list = values.map(player => ({
                        playerId: extractId(player._links.self.href),
                        name: player.name,
                    }));
                    setPlayers(list)
                })
            })
        })
    }, [])

    useEffect(() => {
        const rows = players
            .sort((a, b) => a.name < b.name ? -1 : a.name > b.name)
            .map(player => ({
                playerId: player.playerId,
                playerName: player.name,
                team: "",
                goalsInFavor: 0,
                ownGoals: 0,
                yellowCards: 0,
                blueCards: 0,
                redCards: 0,
            }))
        setFormData((prevFormData) => {
            prevFormData["matches"][0] = {
                "players": [...rows]
            }
            return {...prevFormData}
        })
    }, [players]);

    const handleChange = (event) => {
        const {name, value} = event.target
        setFormData((prevFormData) => ({...prevFormData, [name]: value}))
    }

    const handlePlayerStatisticChange = (event) => {
        const {name, value} = event.target
        const [_, matchIndex, playerStatisticIndex, field] = /^match\.(.*?)\.playerStatistic\.(.*?)\.(.*?)$/.exec(name)
        setFormData((prevFormData) => {
            prevFormData["matches"][matchIndex]["players"][playerStatisticIndex][field] = value
            return {...prevFormData}
        })
    }

    const handleSubmit = (event) => {
        event.preventDefault()
        submitGameday(creationUrl, formData)
            .then(data => data._links.self.href)
            .then(setViewUrl)
    }

    return <div>
        <h1 className={"mb-3"}>Create Gameday</h1>
        <form onSubmit={handleSubmit}>
            <div className={"row g-3 align-items-center"}>
                <div className={"col-auto mb-3"}>
                    <label htmlFor={"inputDate"} className={"col-form-label"}>Date:</label>
                </div>
                <div className={"col-auto mb-3"}>
                    <input type={"date"}
                           id={"inputDate"}
                           className={"form-control"}
                           name={"date"}
                           value={formData.date}
                           onChange={handleChange}
                    />
                </div>
            </div>

            <h2 className={"mb-3"}>Matches</h2>
            {formData.matches.map((match, matchIndex) => {
                    const matchKey = "match." + matchIndex
                    return <table key={matchKey} className={"table"}>
                        <thead>
                        <tr>
                            <th>Player ID</th>
                            <th>Team</th>
                            <th>Goals in Favor</th>
                            <th>Own Goals</th>
                            <th>Yellow Cards</th>
                            <th>Blue Cards</th>
                            <th>Red Cards</th>
                        </tr>
                        </thead>
                        <tbody>
                        {match.players.map((playerStatistic, playerStatisticIndex) => {
                                const playerStatisticKey = matchKey + ".playerStatistic." + playerStatisticIndex
                                return <tr key={playerStatisticKey}>
                                    <td>
                                        <input type={"hidden"}
                                               name={playerStatisticKey + ".playerId"}
                                               value={playerStatistic.playerId}
                                        />
                                        {playerStatistic.playerName}
                                    </td>
                                    <td width={"90px"}>
                                        <input type={"radio"}
                                               className={"btn-check"}
                                               name={playerStatisticKey + ".team"}
                                               id={playerStatisticKey + ".radioTeamA"}
                                               value={"A"}
                                               checked={playerStatistic.team === 'A' ? 'checked' : ''}
                                               onChange={handlePlayerStatisticChange}
                                        />
                                        <label className={"btn btn-secondary"}
                                               htmlFor={playerStatisticKey + ".radioTeamA"}>A</label>

                                        <input type={"radio"}
                                               className={"btn-check"}
                                               name={playerStatisticKey + ".team"}
                                               id={playerStatisticKey + ".radioTeamB"}
                                               value={"B"}
                                               checked={playerStatistic.team === 'B' ? 'checked' : ''}
                                               onChange={handlePlayerStatisticChange}
                                        />
                                        <label className={"btn btn-secondary"}
                                               htmlFor={playerStatisticKey + ".radioTeamB"}>B</label>
                                    </td>
                                    <td>
                                        <input type="number"
                                               id={playerStatisticKey + ".goalsInFavor"}
                                               className={"form-control"}
                                               name={playerStatisticKey + ".goalsInFavor"}
                                               value={playerStatistic.goalsInFavor}
                                               onChange={handlePlayerStatisticChange}
                                        />
                                    </td>
                                    <td>
                                        <label htmlFor={playerStatisticKey + ".ownGoals"}>
                                            <input type="number"
                                                   id={playerStatisticKey + ".ownGoals"}
                                                   className={"form-control"}
                                                   name={playerStatisticKey + ".ownGoals"}
                                                   value={playerStatistic.ownGoals}
                                                   onChange={handlePlayerStatisticChange}
                                            />
                                        </label>
                                    </td>
                                    <td>
                                        <label htmlFor={playerStatisticKey + ".yellowCards"}>
                                            <input type="number"
                                                   id={playerStatisticKey + ".yellowCards"}
                                                   className={"form-control"}
                                                   name={playerStatisticKey + ".yellowCards"}
                                                   value={playerStatistic.yellowCards}
                                                   onChange={handlePlayerStatisticChange}
                                            />
                                        </label>
                                    </td>
                                    <td>
                                        <label htmlFor={playerStatisticKey + ".blueCards"}>
                                            <input type="number"
                                                   id={playerStatisticKey + ".blueCards"}
                                                   className={"form-control"}
                                                   name={playerStatisticKey + ".blueCards"}
                                                   value={playerStatistic.blueCards}
                                                   onChange={handlePlayerStatisticChange}
                                            />
                                        </label>
                                    </td>
                                    <td>
                                        <label htmlFor={playerStatisticKey + ".redCards"}>
                                            <input type="number"
                                                   id={playerStatisticKey + ".redCards"}
                                                   className={"form-control"}
                                                   name={playerStatisticKey + ".redCards"}
                                                   value={playerStatistic.redCards}
                                                   onChange={handlePlayerStatisticChange}
                                            />
                                        </label>
                                    </td>
                                </tr>
                            }
                        )}
                        </tbody>
                    </table>
                }
            )}
            <button type="submit" className={"btn btn-primary"}>Submit</button>
        </form>
    </div>
}

function submitGameday(link, formData) {
    const parsedDate = formData.date + "T00:00:00.000Z"

    formData.matches.forEach((match) => {
        match.players = match.players.filter((player) => player.team !== '')
    })

    return fetch(link, {
        method: 'POST',
        headers: {
            "Accept": "application/hal+json",
            "Content-Type": "application/json"
        },
        mode: "cors",
        body: JSON.stringify({...formData, date: parsedDate, gamedayId: uuidv4()})
    })
        .then(response => response.json())
}

function extractId(url) {
    return url.slice(url.lastIndexOf("/") + 1)
}