import React, {createContext, useEffect, useState} from "react";
import {Link, Outlet} from "react-router-dom";
import {useSessionState} from "../api/useSessionState";
import {fetchUrl} from "../api/fetchUrl";

export default function App() {
    const [amateurSoccerGroupCreationUrl, setAmateurSoccerGroupCreationUrl] = useSessionState("amateurSoccerGroupCreationUrl")
    const [, setAmateurSoccerGroupUserDataCreationUrl] = useSessionState("amateurSoccerGroupUserDataCreationUrl")
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
            viewUrl: {
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

    const [amateurSoccerGroupsLoad, setAmateurSoccerGroupsLoad] = useState(true)
    const [amateurSoccerGroups, setAmateurSoccerGroups] = useState([])
    useEffect(() => {
        if (!amateurSoccerGroupsLoad) return
        setAmateurSoccerGroupsLoad(false)
        fetchAmateurSoccerGroups().then((list) => {
            setAmateurSoccerGroupCreationUrl(list._links?.create?.href)
            setAmateurSoccerGroupUserDataCreationUrl(list._links?.["create-user-data"]?.href)
            Promise.all(list._embedded.amateurSoccerGroups.map(it => fetchUrl(it._links["get-user-data"].href)))
                .then(values => {
                    setAmateurSoccerGroups(values
                        .sort((a, b) => a.name < b.name ? -1 : a.name > b.name)
                        .map((userData, index) => ({
                            name: userData.name,
                            url: list._embedded.amateurSoccerGroups
                                .filter((row) => row._links["get-user-data"].href === userData._links["self"].href)
                                .map((row) => row._links["self"].href)
                        }))
                    )
                })
        })
    }, [])

    return (
        <MenuContext.Provider value={menu}>
            <AmateurSoccerGroupsLoadContext.Provider value={{
                value: amateurSoccerGroupsLoad,
                set: setAmateurSoccerGroupsLoad
            }}>
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
                                                          setAmateurSoccerGroupViewUrl(amateurSoccerGroup.url)
                                                      }}
                                                      className="dropdown-item">
                                                    {amateurSoccerGroup.name}
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
            </AmateurSoccerGroupsLoadContext.Provider>
        </MenuContext.Provider>
    )
}

export const MenuContext = createContext(null)
export const AmateurSoccerGroupsLoadContext = createContext(null)

function fetchAmateurSoccerGroups() {
    return fetch("http://localhost:8080/api/amateurSoccerGroups", {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    }).then(response => response.json())
}
