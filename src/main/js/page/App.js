import React, {createContext, useEffect, useState} from "react";
import {Link, Outlet} from "react-router-dom";
import {useSessionState} from "../api/useSessionState";

export default function App() {
    const [amateurSoccerGroupCreationUrl, setAmateurSoccerGroupCreationUrl] = useSessionState("amateurSoccerGroupCreationUrl")
    const [amateurSoccerGroupViewUrl, setAmateurSoccerGroupViewUrl] = useSessionState("amateurSoccerGroupViewUrl")
    const [amateurSoccerGroupGamedaysUrl, setAmateurSoccerGroupGamedaysUrl] = useSessionState("amateurSoccerGroupGamedaysUrl")
    const [amateurSoccerGroupGamedaysCreationUrl, setAmateurSoccerGroupGamedaysCreationUrl] = useSessionState("amateurSoccerGroupGamedaysCreationUrl")
    const [amateurSoccerGroupCalculateRankingUrl, setAmateurSoccerGroupCalculateRankingUrl] = useSessionState("amateurSoccerGroupCalculateRankingUrl")
    const [amateurSoccerGroupPlayersUrl, setAmateurSoccerGroupPlayersUrl] = useSessionState("amateurSoccerGroupPlayersUrl")
    const menu = {
        amateurSoccerGroup: {
            creationUrl: {
                value: amateurSoccerGroupCreationUrl,
                set: setAmateurSoccerGroupCreationUrl,
            },
            id: {
                value: amateurSoccerGroupViewUrl,
                set: setAmateurSoccerGroupViewUrl,
            },
            gamedaysUrl: {
                value: amateurSoccerGroupGamedaysUrl,
                set: setAmateurSoccerGroupGamedaysUrl,
            },
            gamedaysCreationUrl: {
                value: amateurSoccerGroupGamedaysCreationUrl,
                set: setAmateurSoccerGroupGamedaysCreationUrl,
            },
            calculateRankingUrl: {
                value: amateurSoccerGroupCalculateRankingUrl,
                set: setAmateurSoccerGroupCalculateRankingUrl,
            },
            playersUrl: {
                value: amateurSoccerGroupPlayersUrl,
                set: setAmateurSoccerGroupPlayersUrl,
            }
        }
    }

    const [amateurSoccerGroups, setAmateurSoccerGroups] = useState([])
    useEffect(() => {
        fetchAmateurSoccerGroups()
            .then(response => response.data.allAmateurSoccerGroups)
            .then((allAmateurSoccerGroups) => {
                setAmateurSoccerGroupCreationUrl(allAmateurSoccerGroups.actions.filter(action => action === 'create').length === 1)
                setAmateurSoccerGroups(allAmateurSoccerGroups.data)
            })
    }, [])

    return (
        <MenuContext.Provider value={menu}>
            <nav className="navbar navbar-expand-lg bg-body-tertiary">
                <div className="container-fluid">
                    <Link to="/" className="navbar-brand">Home</Link>
                    <button className="navbar-toggler"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#navbarSupportedContent"
                            aria-controls="navbarSupportedContent"
                            aria-expanded="false"
                            aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-item dropdown">
                                <Link to="#"
                                      className="nav-link dropdown-toggle"
                                      role="button"
                                      data-bs-toggle="dropdown"
                                      aria-haspopup="true"
                                      aria-expanded="false">
                                    Amateur Soccer Groups
                                </Link>
                                <ul className="dropdown-menu" aria-labelledby="navbarDropdown">
                                    {amateurSoccerGroups.map((amateurSoccerGroup, amateurSoccerGroupIndex) =>
                                        <li key={amateurSoccerGroupIndex}>
                                            <Link to="/amateurSoccerGroups/view"
                                                  onClick={() => {
                                                      setAmateurSoccerGroupViewUrl(amateurSoccerGroup.id)
                                                  }}
                                                  className="dropdown-item">
                                                {amateurSoccerGroup.userData.name}
                                            </Link>
                                        </li>)}
                                    {amateurSoccerGroups.length > 0 && amateurSoccerGroupCreationUrl &&
                                        <li>
                                            <hr className="dropdown-divider"/>
                                        </li>}
                                    {amateurSoccerGroupCreationUrl &&
                                        <Link to="/amateurSoccerGroups/new" className="dropdown-item">New</Link>}
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
            <div className={"m-3"}>
                <Outlet/>
            </div>
        </MenuContext.Provider>
    )
}

export const MenuContext = createContext(null)

function fetchAmateurSoccerGroups() {
    return fetch("http://localhost:8080/graphql", {
        method: 'POST',
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        mode: "cors",
        body: JSON.stringify({
            query: `{
          allAmateurSoccerGroups {
            data {
              id
              userData {
                name
              }
              actions
            }
            actions
          }
        }`
        })
    }).then(response => response.json())
}
