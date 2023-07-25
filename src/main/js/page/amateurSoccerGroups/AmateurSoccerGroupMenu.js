import React, {useEffect, useState} from "react";
import {Link, useLocation} from "react-router-dom";
import {fetchUrl} from "../../api/fetchUrl";

export function AmateurSoccerGroupMenu(
    {menu}
) {
    const gamedaysUrl = menu.amateurSoccerGroup.gamedaysUrl.value;
    const location = useLocation()
    const isAmateurSoccerGroupNewUrl = location.pathname.includes('/amateurSoccerGroups/new');
    const playersUrl = menu.amateurSoccerGroup.playersUrl.value;

    const [_gamedays, setGamedays] = useState([])
    useEffect(() => {
        if (gamedaysUrl === undefined) return
        fetchUrl(gamedaysUrl).then(data => {
            menu.amateurSoccerGroup.viewUrl.set(data._links?.["get-amateur-soccer-group"]?.href)
            let sortedGamedays = data._embedded?.gamedays?.sort((a, b) => (a.date > b.date) - (a.date < b.date)) || []
            setGamedays(sortedGamedays)
        })
    }, [gamedaysUrl])

    return !isAmateurSoccerGroupNewUrl &&
        <nav className="navbar navbar-expand-lg bg-primary">
            <div className="container-fluid">
                <Link to="/amateurSoccerGroups/view" className="navbar-brand">Amateur Soccer Group</Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                        aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav">
                        {gamedaysUrl &&
                            <li className="nav-item dropdown">
                                <Link to="/gamedays"
                                      className="nav-link dropdown-toggle"
                                      role="button"
                                      data-bs-toggle="dropdown"
                                      aria-haspopup="true"
                                      aria-expanded="false">
                                    Gamedays
                                </Link>
                                <div className="dropdown-menu" aria-labelledby="navbarDropdown">
                                    {menu.amateurSoccerGroup.gamedaysCreationUrl.set &&
                                        <Link to="/gamedays/new"
                                              className="dropdown-item">
                                            Register Gameday
                                        </Link>}
                                    {menu.amateurSoccerGroup.calculateRankingUrl.value &&
                                        <Link to="/amateurSoccerGroups/ranking"
                                              className="dropdown-item">
                                            Calculate Ranking
                                        </Link>}
                                </div>
                            </li>}
                        {playersUrl &&
                            <li className="nav-item">
                                <Link to="/amateurSoccerGroups/players"
                                      className="nav-link">
                                    Players
                                </Link>
                            </li>}
                    </ul>
                </div>
            </div>
        </nav>
}