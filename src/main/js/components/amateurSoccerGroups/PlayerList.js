import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";

import {fetchGraphQL} from "../../api/fetchGraphQL";

export function PlayerList(
    {amateurSoccerGroupId, toPlayerNew}
) {
    const [players, setPlayers] = useState([])
    const [hasPlayerCreationAction, setHasPlayerCreationAction] = useState(false)

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
                setHasPlayerCreationAction(players.actions.filter(action => action === 'create').length === 1)
                setPlayers(players.data)
            })
    }, [amateurSoccerGroupId])

    return <div>
        <h1>
            Players
            {hasPlayerCreationAction &&
                <Link to="#"
                      onClick={(e) => {
                          e.preventDefault()
                          toPlayerNew()
                      }}
                      className="btn btn-primary">
                    New
                </Link>
            }
        </h1>
        <table className="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Name</th>
            </tr>
            </thead>
            <tbody>
            {players.length > 0 && players
                .sort((a, b) => a.userData.name < b.userData.name ? -1 : a.userData.name > b.userData.name)
                .map((player, playerIndex) =>
                    <tr key={playerIndex}>
                        <th scope="row">{playerIndex + 1}</th>
                        <td>{player.userData.name}</td>
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