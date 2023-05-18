import React, {useState} from "react";

export function CalculateRanking(
    {uri}
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
        submitCalculateRanking(uri, formData).then(setResult)
    }

    return <div>
        <h1>Calculate Ranking</h1>

        <form onSubmit={handleSubmit}>
            <label htmlFor="from">
                From:
                <input id="from" type="date" name="from" value={formData.from}
                       onChange={handleChange}/>
            </label>
            <label htmlFor="to">
                To:
                <input id="to" type="date" name="to" value={formData.to} onChange={handleChange}/>
            </label>
            <p>
                <button type="submit">Calculate</button>
            </p>
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
                    <td>{playerStatistic.playerId}</td>
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