import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import classNames from "classnames";
import {fetchGraphQL} from "../../api/fetchGraphQL";

export function GamedayList(
    {amateurSoccerGroupId, creationRedirectWhenEmptyUrl}
) {
    const [gameday, setGameday] = useState()
    const [hasPreviousGameday, setPreviousGameday] = useState(false)
    const [hasNextGameday, setNextGameday] = useState(false)
    const [currentMatchIndex, setCurrentMatchIndex] = useState(0)
    const [pageNumber, setPageNumber] = useState(0)

    const fetchGameday = () => fetchGraphQL(`{
          gamedays(
            amateurSoccerGroupId: "${amateurSoccerGroupId}"
            page: ${pageNumber}
            size: 1
            sort: "date"
            direction: DESC
          ) {
            data {
              date
              matches {
                players {
                  playerId
                  userData {
                    name
                  }
                  team
                  goalsInFavor
                  ownGoals
                  yellowCards
                  blueCards
                  redCards
                }
              }
              actions
            }
            actions
            page {
              size
              totalElements
              totalPages
              number
            }
          }
        }`)
        .then(response => response.data.gamedays)
        .then(gamedays => {
            setPreviousGameday(gamedays.actions.filter(action => action === "next").length === 1)
            setNextGameday(gamedays.actions.filter(action => action === "prev").length === 1)
            setGameday(gamedays.data[0])
        })

    useEffect(() => void fetchGameday(amateurSoccerGroupId), [amateurSoccerGroupId, pageNumber])

    return <div>
        <h1>Gameday</h1>

        {!gameday && <p>
            No game days registered.
            {creationRedirectWhenEmptyUrl &&
                <span> Click <Link to={creationRedirectWhenEmptyUrl}>here</Link> to register one</span>}
        </p>}
        <div className="row justify-content-between">
            <div className="col-4">
                {hasPreviousGameday &&
                    <Link to="#"
                          onClick={() => {
                              setCurrentMatchIndex(0)
                              setPageNumber((prevState) => prevState + 1);
                          }}
                          className="btn btn-primary"
                    >Previous</Link>}
            </div>
            <div className="col-4">
                {hasNextGameday &&
                    <Link
                        to="#"
                        onClick={() => {
                            setCurrentMatchIndex(0)
                            setPageNumber((prevState) => prevState - 1);
                        }}
                        className="btn btn-primary"
                    >Next</Link>}
            </div>
        </div>
        {gameday && <div>
            <div className="form-group row">
                <label htmlFor="staticEmail" className="col-sm-2 col-form-label">Date</label>
                <div className="col-sm-10">
                    <input readOnly={true} className="form-control-plaintext" value={gameday.date}/>
                </div>
            </div>
            <nav aria-label="Match pagination">
                <ul className="pagination">
                    <li className={classNames({
                        "page-item": true,
                        disabled: currentMatchIndex === 0
                    })}>
                        <Link
                            to="#"
                            onClick={() => setCurrentMatchIndex(currentMatchIndex - 1)}
                            className="page-link"
                        >Previous</Link>
                    </li>
                    {gameday.matches.map((_, matchIndex) => <li className="page-item" key={matchIndex}>
                            <Link
                                to="#"
                                onClick={() => setCurrentMatchIndex(matchIndex)}
                                className={
                                    "page-link "
                                    + (matchIndex === currentMatchIndex ? "active" : "")
                                }
                            > {matchIndex + 1} </Link>
                        </li>
                    )}
                    <li className={classNames({
                        "page-item": true,
                        disabled: currentMatchIndex === gameday.matches.length - 1
                    })}>
                        <Link
                            to="#"
                            onClick={() => setCurrentMatchIndex(currentMatchIndex + 1)}
                            className="page-link"
                        >Next</Link>
                    </li>
                </ul>
            </nav>

            <table className="table">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th>team</th>
                    <th>Goals in Favor</th>
                    <th>Own Goals</th>
                    <th>Yellow Cards</th>
                    <th>Blue Cards</th>
                    <th>Red Cards</th>
                </tr>
                </thead>
                <tbody>
                {gameday?.matches?.[currentMatchIndex].players
                    .sort((a, b) => a.userData.name < b.userData.name ? -1 : a.userData.name > b.userData.name)
                    .map(player => (<tr key={`${currentMatchIndex}-${player.playerId}`}>
                        <th scope="row">{player.userData.name}</th>
                        <td>{player.team}</td>
                        <td>{player.goalsInFavor}</td>
                        <td>{player.ownGoals}</td>
                        <td>{player.yellowCards}</td>
                        <td>{player.blueCards}</td>
                        <td>{player.redCards}</td>
                    </tr>))}
                </tbody>
            </table>
        </div>}
    </div>
}