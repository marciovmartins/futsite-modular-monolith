import React, {useEffect, useState} from "react";
import {fetchUrl} from "../../api/fetchUrl";
import {Link} from "react-router-dom";

export function PlayerList(
    {url, toPlayerNew, setCreationUrl}
) {
    const [players, setPlayers] = useState([])

    useEffect(() => {
        if (url === undefined) return
        fetchUrl(url)
            .then(response => {
                setCreationUrl(response._links?.create?.href)
                return response
            })
            .then(response => response._embedded?.players)
            .then(players => players?.flatMap(player => player._links["get-player-user-data"].href))
            .then(playerUserDataUrls => {
                if (playerUserDataUrls)
                    Promise.all(playerUserDataUrls.map(fetchUrl)).then(setPlayers)
            })
    }, [url])

    return <div>
        <h1>
            Players
            <Link to="#"
                  onClick={(e) => {
                      e.preventDefault()
                      toPlayerNew()
                  }}
                  className="btn btn-primary">
                New
            </Link>
        </h1>
        <table className="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Name</th>
            </tr>
            </thead>
            <tbody>
            {players.length > 0 && players.map((player, playerIndex) =>
                <tr key={playerIndex}>
                    <th scope="row">{playerIndex + 1}</th>
                    <td>{player.name}</td>
                </tr>
            )}
            {players.length === 0 &&
                <tr>
                    <th colSpan="2">
                        No players added.
                        {toPlayerNew &&
                            <span> Click <Link to="#"
                                               onClick={(e) => {
                                                   e.preventDefault()
                                                   toPlayerNew()
                                               }}>here</Link> to register one</span>
                        }
                    </th>
                </tr>
            }
            </tbody>
        </table>
    </div>
}