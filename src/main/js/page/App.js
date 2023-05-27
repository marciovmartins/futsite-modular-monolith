import React, {createContext} from "react";
import {Link, Outlet} from "react-router-dom";
import {useSessionState} from "../api/useSessionState";

export default function App() {
    const [amateurSoccerGroupCreationUrl, setAmateurSoccerGroupCreationUrl] = useSessionState("amateurSoccerGroupCreationUrl")
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
    return (
        <MenuContext.Provider value={menu}>
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
                        <li className="nav-item">
                            <Link to="/amateurSoccerGroups" className="nav-link">Amateur Soccer Groups</Link>
                        </li>
                        {menu.amateurSoccerGroup.viewUrl.value &&
                            <li className="nav-item dropdown">
                                <a className="nav-link dropdown-toggle"
                                   href="#"
                                   role="button"
                                   data-toggle="dropdown"
                                   aria-haspopup="true"
                                   aria-expanded="false">
                                    Current Amateur Soccer Group
                                </a>
                                <div className="dropdown-menu" aria-labelledby="navbarDropdown">
                                    <Link to={"/amateurSoccerGroups/view"}
                                          className="dropdown-item">
                                        Amateur Soccer Group
                                    </Link>
                                    {amateurSoccerGroupGamedaysUrl &&
                                        <Link to={"/gamedays"}
                                              className="dropdown-item">
                                            Gamedays
                                        </Link>}
                                    {amateurSoccerGroupCalculateRankingUrl &&
                                        <Link to={"/amateurSoccerGroups/ranking"}
                                              className="dropdown-item">
                                            Ranking
                                        </Link>}
                                </div>
                            </li>}
                    </ul>
                </div>
            </nav>
            <Outlet/>
        </MenuContext.Provider>
    )
}

export const MenuContext = createContext(null)