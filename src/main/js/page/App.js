import React, {createContext, useEffect, useState} from "react";
import {Link, Outlet} from "react-router-dom";
import {useSessionState} from "../api/useSessionState";
import {fetchUrl} from "../api/fetchUrl";

export default function App() {
    const [amateurSoccerGroupCreationUrl, setAmateurSoccerGroupCreationUrl] = useSessionState("amateurSoccerGroupCreationUrl")
    const [, setAmateurSoccerGroupUserDataCreationUrl] = useSessionState("amateurSoccerGroupUserDataCreationUrl")
    const [amateurSoccerGroupViewUrl, setAmateurSoccerGroupViewUrl] = useSessionState("amateurSoccerGroupViewUrl")
    const [amateurSoccerGroupGamedaysUrl, setAmateurSoccerGroupGamedaysUrl] = useSessionState("amateurSoccerGroupGamedaysUrl")
    const [amateurSoccerGroupCalculateRankingUrl, setAmateurSoccerGroupCalculateRankingUrl] = useSessionState("amateurSoccerGroupCalculateRankingUrl")
    const menu = {
        amateurSoccerGroup: {
            creationUrl: {
                value: amateurSoccerGroupCreationUrl,
                set: setAmateurSoccerGroupCreationUrl
            },
            viewUrl: {
                value: amateurSoccerGroupViewUrl,
                set: setAmateurSoccerGroupViewUrl
            },
            gamedaysUrl: {
                value: amateurSoccerGroupGamedaysUrl,
                set: setAmateurSoccerGroupGamedaysUrl
            },
            calculateRankingUrl: {
                value: amateurSoccerGroupCalculateRankingUrl,
                set: setAmateurSoccerGroupCalculateRankingUrl
            },
        }
    }

    const [amateurSoccerGroupsLoad, setAmateurSoccerGroupsLoad] = useState(true)
    const [amateurSoccerGroups, setAmateurSoccerGroups] = useState([])
    useEffect(() => {
        if (!amateurSoccerGroupsLoad) return
        fetchAmateurSoccerGroups().then((list) => {
            setAmateurSoccerGroupCreationUrl(list._links?.create?.href)
            setAmateurSoccerGroupUserDataCreationUrl(list._links?.["create-user-data"]?.href)
            Promise.all(list._embedded.amateurSoccerGroups.map(it => fetchUrl(it._links["get-user-data"].href)))
                .then(values => {
                    setAmateurSoccerGroups(values
                        .sort((a, b) => a.name.localeCompare(b.name))
                        .map((userData, index) => ({
                            name: userData.name,
                            url: list._embedded.amateurSoccerGroups[index]._links.self.href
                        }))
                    )
                    setAmateurSoccerGroupsLoad(false)
                })
        })
    }, [amateurSoccerGroupsLoad])

    return (
        <MenuContext.Provider value={menu}>
            <AmateurSoccerGroupsLoadContext.Provider value={{
                value: amateurSoccerGroupsLoad,
                set: setAmateurSoccerGroupsLoad
            }}>
                <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                    <Link to="/" className="navbar-brand">Home</Link>
                    <button className="navbar-toggler"
                            type="button"
                            data-toggle="collapse"
                            data-target="#navbarSupportedContent"
                            aria-controls="navbarSupportedContent"
                            aria-expanded="false"
                            aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav mr-auto">
                            <li className="nav-item dropdown">
                                <Link to="#"
                                      className="nav-link dropdown-toggle"
                                      role="button"
                                      data-toggle="dropdown"
                                      aria-haspopup="true"
                                      aria-expanded="false">
                                    Amateur Soccer Groups
                                </Link>
                                <div className="dropdown-menu" aria-labelledby="navbarDropdown">
                                    {amateurSoccerGroups.map((amateurSoccerGroup, amateurSoccerGroupIndex) =>
                                        <Link to="/amateurSoccerGroups/view"
                                              key={amateurSoccerGroupIndex}
                                              onClick={() => {
                                                  setAmateurSoccerGroupViewUrl(amateurSoccerGroup.url)
                                              }}
                                              className="dropdown-item">
                                            {amateurSoccerGroup.name}
                                        </Link>)}
                                    {amateurSoccerGroupCreationUrl &&
                                        <Link to="/amateurSoccerGroups/new" className="dropdown-item">New</Link>}
                                </div>
                            </li>
                        </ul>
                    </div>
                </nav>
                <Outlet/>
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
