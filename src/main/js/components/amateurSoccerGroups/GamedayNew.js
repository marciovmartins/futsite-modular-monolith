import React, {useEffect, useState} from "react";
import {v4 as uuidv4} from "uuid";
import {Link} from "react-router-dom";

import {fetchGraphQL} from "../../api/fetchGraphQL";

export function GamedayNew(
    {setViewId, amateurSoccerGroupId, playersCreationUrl}
) {
    const [formData, setFormData] = useState({
        date: '',
        matches: [],
    })

    const [players, setPlayers] = useState([])

    useEffect(() => {
        fetchGraphQL(`{
            playersByAmateurSoccerGroupId(amateurSoccerGroupId: "${amateurSoccerGroupId}") {
                data {
                    id
                    userData {
                        name
                    }
                    actions
                }
                actions
            }
        }`)
            .then(response => response.data.playersByAmateurSoccerGroupId)
            .then(players => {
                setPlayers(players.data)
            })
    }, [amateurSoccerGroupId])

    useEffect(() => {
        const rows = players
            .map(player => ({
                playerId: player.id,
                playerName: player.userData.name,
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
        const [_, matchIndex, playerId, field] = /^match\.(.*?)\.playerStatistic\.(.*?)\.(.*?)$/.exec(name)
        setFormData((prevFormData) => {
            prevFormData["matches"][matchIndex]["players"][playerId][field] = value
            return {...prevFormData}
        })
    }

    const handleSubmit = (event) => {
        event.preventDefault()
        submitGameday(amateurSoccerGroupId, formData)
            .then(response => response.data.registerGameday.id)
            .then(setViewId)
    }

    return <div>
        <h1 className={"mb-3"}>Create Gameday</h1>

        {players.length === 0 && <p>
            No players registered.
            {playersCreationUrl &&
                <span> Click <Link to={playersCreationUrl}>here</Link> to register one</span>}
        </p>}
        {players.length > 0 &&
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
                            {match.players
                                .sort((a, b) => a.playerName < b.playerName ? -1 : a.playerName > b.playerName)
                                .map((playerStatistic, playerStatisticIndex) => {
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
                                                       checked={playerStatistic.team === 'A'}
                                                       onChange={handlePlayerStatisticChange}
                                                />
                                                <label className={"btn btn-secondary"}
                                                       htmlFor={playerStatisticKey + ".radioTeamA"}>A</label>

                                                <input type={"radio"}
                                                       className={"btn-check"}
                                                       name={playerStatisticKey + ".team"}
                                                       id={playerStatisticKey + ".radioTeamB"}
                                                       value={"B"}
                                                       checked={playerStatistic.team === 'B'}
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
            </form>}
    </div>
}

function submitGameday(amateurSoccerGroupId, formData) {
    const parsedDate = formData.date + "T00:00:00.000Z";

    const matchesGraphQL = formData.matches.map((match) => {
        const playersGraphQL = match.players
            .filter((player) => player.team !== '')
            .map(player => `
                    {
                      playerId: "${player.playerId}"
                      team: "${player.team}"
                      goalsInFavor: ${player.goalsInFavor}
                      ownGoals: ${player.ownGoals}
                      yellowCards: ${player.yellowCards}
                      blueCards: ${player.blueCards}
                      redCards: ${player.redCards}
                    }
            `)
        return `            {
              players: [${playersGraphQL.join()}]
            }`;
    })

    const registerGamedayMutation = `
        mutation {
          registerGameday(
            id: "${uuidv4()}"
            amateurSoccerGroupId: "${amateurSoccerGroupId}"
            date: "${parsedDate}"
            matches: [${matchesGraphQL.join()}]
          ) {
            id
          }
        }
    `;

    return fetchGraphQL(registerGamedayMutation)
}