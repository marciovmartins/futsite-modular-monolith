import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {fetchUrl} from "../../api/fetchUrl";
import classNames from "classnames";

//TODO: remove hard-coded <Link> "to", should be uplifted

export function GamedayList(
    {url, setCreationUrl, creationUrl, setAmateurSoccerGroupUrl}
) {
    const [gameday, setGameday] = useState()
    const [previousGamedayUrl, setPreviousGamedayUrl] = useState()
    const [nextGamedayUrl, setNextGamedayUrl] = useState()
    const [currentMatchIndex, setCurrentMatchIndex] = useState(0)
    const currentMatch = gameday?.matches?.[currentMatchIndex]

    const fetchGameday = (url) => fetchUrl(url).then((data) => {
        if (data === undefined) return
        setCreationUrl(data._links?.["create-gameday"]?.href)
        setAmateurSoccerGroupUrl(data._links?.["get-amateur-soccer-group"]?.href)
        setPreviousGamedayUrl(data._links?.["next"]?.href)
        setNextGamedayUrl(data._links?.["prev"]?.href)

        fetchUrl(data._embedded?.gamedays?.[0]._links.self.href)
            .then(gameday => {
                const playersUserDataHref = gameday.matches
                    .flatMap(match => match.players)
                    .map(player => player.playerId)
                    .map(playerId => gameday._links["get-player-user-data-" + playerId].href)
                Promise.all(playersUserDataHref.map(fetchUrl)).then(values => {
                    gameday.matches.forEach(match => {
                        match.players.forEach(playerStatistic => {
                            const player = values.find(it => it._links.self.href.includes(playerStatistic.playerId))
                            playerStatistic.playerName = player.name
                        })
                        match.players = match.players.sort((a, b) => a.playerName < b.playerName ? -1 : a.playerName > b.playerName)
                    })
                    setGameday(gameday)
                })
            })
    })

    useEffect(() => {
        if (url === undefined) return
        void fetchGameday(url + "?size=1&sort=date,desc")
    }, [url])

    return <div>
        <h1>Gameday</h1>

        {!gameday && <p>
            No game days registered.
            {creationUrl
                && <span> Click <Link to="/gamedays/new" state={{gamedaysLink: url}}>here</Link> to register one</span>}
        </p>}
        <div className="row justify-content-between">
            <div className="col-4">
                {previousGamedayUrl &&
                    <Link to="#"
                          onClick={() => fetchGameday(previousGamedayUrl)}
                          className="btn btn-primary"
                    >Previous</Link>}
            </div>
            <div className="col-4">
                {nextGamedayUrl &&
                    <Link
                        to="#"
                        onClick={() => fetchGameday(nextGamedayUrl)}
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
                                className="page-link"
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
                {currentMatch.players.map((player, playerIndex) => <tr key={playerIndex}>
                    <th scope="row">{player.playerName}</th>
                    <td>{player.team}</td>
                    <td>{player.goalsInFavor}</td>
                    <td>{player.ownGoals}</td>
                    <td>{player.yellowCards}</td>
                    <td>{player.blueCards}</td>
                    <td>{player.redCards}</td>
                </tr>)}
                </tbody>
            </table>
        </div>}
    </div>
}