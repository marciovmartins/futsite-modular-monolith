import React, {useState} from "react";
import {fetchGraphQL} from "../../api/fetchGraphQL";

export function CalculateRanking(
    {amateurSoccerGroupId}
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
        submitCalculateRanking(amateurSoccerGroupId, formData)
            .then(response => setResult(response.data.calculateRanking.content))
    }

    return <div>
        <h1 className={"mb-3"}>Calculate Ranking</h1>
        <form onSubmit={handleSubmit}>
            <div className={"row g-3 align-items-center"}>
                <div className={"col-auto mb-3"}>
                    <label htmlFor="inputFrom">From:</label>
                </div>
                <div className={"col-auto mb-3"}>
                    <input type={"date"}
                           className={"form-control"}
                           id={"inputFrom"}
                           name={"from"}
                           value={formData.from}
                           onChange={handleChange}
                    />
                </div>
                <div className={"col-auto mb-3"}>
                    <label htmlFor={"inputTo"}>To:</label>
                </div>
                <div className={"col-auto mb-3"}>
                    <input type={"date"}
                           className={"form-control"}
                           id={"inputTo"}
                           name={"to"}
                           value={formData.to}
                           onChange={handleChange}
                    />
                </div>
                <div className={"col-auto mb-3"}>
                    <button type="submit" className="btn btn-primary">Calculate</button>
                </div>
            </div>

        </form>

        {result && <div>
            <h2>Result</h2>

            <p>Period: {formData.from} to {formData.to}</p>
            <p>Matches: {result.matches}</p>

            {result.playerStatistics.length > 0 &&
                <table width="100%" className={"table table-hover"}>
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
                    <tbody className={"table-group-divider"}>
                    {result.playerStatistics.map((playerStatistic, playerStatisticIndex) => <tr
                        key={playerStatisticIndex}>
                        <td>{playerStatistic.userData.name}</td>
                        <td>{playerStatistic.matches}</td>
                        <td>{playerStatistic.victories}</td>
                        <td>{playerStatistic.draws}</td>
                        <td>{playerStatistic.defeats}</td>
                        <td>{playerStatistic.goalsInFavor}</td>
                        <td>{playerStatistic.ownGoals}</td>
                    </tr>)}
                    </tbody>
                </table>
            }
        </div>}
    </div>
}

function submitCalculateRanking(amateurSoccerGroupId, formData) {
    const parsedStartPeriodDate = formData.from + "T00:00:00.000Z"
    const parsedEndPeriodDate = formData.to + "T23:59:59.999Z"
    return fetchGraphQL(`
        {
          calculateRanking(
            amateurSoccerGroupId: "${amateurSoccerGroupId}"
            period: {from: "${parsedStartPeriodDate}", to: "${parsedEndPeriodDate}"}
          ) {
            content {
              amateurSoccerGroupId
              matches
              playerStatistics {
                playerId
                userData {
                  name
                }
                matches
                victories
                draws
                defeats
                goalsInFavor
                ownGoals
              }
            }
          }
        }
    `)
}