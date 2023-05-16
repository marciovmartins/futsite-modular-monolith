import React, {useState} from "react";
import {v4 as uuidv4} from "uuid";
import {useNavigate} from "react-router-dom";

export function GamedayNew(
    {creationLink, setViewLink}
) {
    const navigate = useNavigate()

    const [formData, setFormData] = useState({
        date: '',
        matches: [
            {
                players: [
                    {
                        playerId: "",
                        team: "",
                        goalsInFavor: 0,
                        ownGoals: 0,
                        yellowCards: 0,
                        blueCards: 0,
                        redCards: 0,
                    },
                    {
                        playerId: "",
                        team: "",
                        goalsInFavor: 0,
                        ownGoals: 0,
                        yellowCards: 0,
                        blueCards: 0,
                        redCards: 0,
                    }
                ]
            }
        ],
    })

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
        submitGameday(creationLink, formData).then(data => {
            setViewLink(data._links.self.href)
            navigate("/gamedays/view")
        })
    }

    return <div>
        <h1>Create Gameday</h1>
        <form onSubmit={handleSubmit}>
            <label htmlFor="date">
                Date:
                <input type="date" id="date" name="date" value={formData.date} onChange={handleChange}/>
            </label>

            <h2>Matches</h2>
            {formData.matches.map((match, matchIndex) => {
                    const matchKey = "match." + matchIndex
                    return <table key={matchKey}>
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
                                        <label htmlFor={playerStatisticKey + ".playerId"}>
                                            <input type="text"
                                                   id={playerStatisticKey + ".playerId"}
                                                   name={playerStatisticKey + ".playerId"}
                                                   value={playerStatistic.playerId}
                                                   onChange={handlePlayerStatisticChange}
                                            />
                                        </label>
                                    </td>
                                    <td>
                                        <label htmlFor={playerStatisticKey + ".team"}>
                                            <select id={playerStatisticKey + ".team"}
                                                    name={playerStatisticKey + ".team"}
                                                    value={playerStatistic.team}
                                                    onChange={handlePlayerStatisticChange}>
                                                <option value=""></option>
                                                <option value="A">A</option>
                                                <option value="B">B</option>
                                            </select>
                                        </label>
                                    </td>
                                    <td>
                                        <label htmlFor={playerStatisticKey + ".goalsInFavor"}>
                                            <input type="number"
                                                   id={playerStatisticKey + ".goalsInFavor"}
                                                   name={playerStatisticKey + ".goalsInFavor"}
                                                   value={playerStatistic.goalsInFavor}
                                                   onChange={handlePlayerStatisticChange}
                                            />
                                        </label>
                                    </td>
                                    <td>
                                        <label htmlFor={playerStatisticKey + ".ownGoals"}>
                                            <input type="number"
                                                   id={playerStatisticKey + ".ownGoals"}
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
            <p>
                <button type="submit">Submit</button>
            </p>
        </form>
    </div>
}

function submitGameday(link, formData) {
    const parsedDate = formData.date + "T00:00:00.000Z"
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