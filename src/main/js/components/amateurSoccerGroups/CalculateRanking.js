import React, {useState} from "react";

export function CalculateRanking(
    {url}
) {
    const [formData, setFormData] = useState({
        from: '',
        to: ''
    })
    const [result, setResult] = useState()

    const handleChange = (event) => {
        const {name, value} = event.target
        setFormData((prevFormData) => ({...prevFormData, [name]: value}))
    }

    const handleSubmit = (event) => {
        event.preventDefault()
        submitCalculateRanking(url, formData).then(response => {
            Promise.all(Object.entries(response._links)
                .filter(entry => entry[0].includes("get-player-user-data-"))
                .map(entry => fetchUrl(entry[1].href)))
                .then(values => {
                    response.playerStatistics.forEach(playerStatistic => {
                        const player = values.find((it) => it._links.self.href.includes(playerStatistic.playerId))
                        playerStatistic.playerName = player.name
                    })
                    setResult(response)
                })
        })
    }

    return <div>
        <h1>Calculate Ranking</h1>

        <form onSubmit={handleSubmit} className="form-inline">
            <label htmlFor="from">From: </label>
            <input id="from"
                   type="date"
                   name="from"
                   className="form-control mx-sm-3"
                   value={formData.from}
                   onChange={handleChange}/>

            <label htmlFor="to">To: </label>
            <input id="to"
                   type="date"
                   name="to"
                   className="form-control mx-sm-3"
                   value={formData.to}
                   onChange={handleChange}/>
            <button type="submit" className="btn btn-primary">Calculate</button>
        </form>

        {result && <div>
            <h2>Result</h2>

            <p>Period: {result.period.from} to {result.period.to}</p>
            <p>Matches: {result.matches}</p>

            <table width="100%">
                <thead>
                <tr>
                    <th>PlayerId</th>
                    <th>Matches</th>
                    <th>Victories</th>
                    <th>Draws</th>
                    <th>Defeats</th>
                    <th>Goals in Favor</th>
                    <th>Own Goals</th>
                </tr>
                </thead>
                <tbody>
                {result.playerStatistics.map((playerStatistic, playerStatisticIndex) => <tr key={playerStatisticIndex}>
                    <td>{playerStatistic.playerName}</td>
                    <td>{playerStatistic.matches}</td>
                    <td>{playerStatistic.victories}</td>
                    <td>{playerStatistic.draws}</td>
                    <td>{playerStatistic.defeats}</td>
                    <td>{playerStatistic.goalsInFavor}</td>
                    <td>{playerStatistic.ownGoals}</td>
                </tr>)}
                </tbody>
            </table>
        </div>}
    </div>
}

function submitCalculateRanking(link, formData) {
    const parsedStartPeriodDate = formData.from + "T00:00:00.000Z"
    const parsedEndPeriodDate = formData.to + "T23:59:59.999Z"
    return fetch(link, {
        method: 'POST',
        headers: {
            "Accept": "application/hal+json",
            "Content-Type": "application/json"
        },
        mode: "cors",
        body: JSON.stringify({from: parsedStartPeriodDate, to: parsedEndPeriodDate})
    })
        .then(response => response.json())
}

function fetchUrl(link) {
    return fetch(link, {
        method: 'GET',
        headers: {
            "Accept": "application/hal+json",
            "Content-Type": "application/json"
        },
        mode: "cors"
    }).then(response => response.json())
}