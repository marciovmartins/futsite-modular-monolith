import React from "react";
import {Link, useLocation} from "react-router-dom";

export function AmateurSoccerGroupMenu(
    {menu}
) {
    const gamedaysUrl = menu.amateurSoccerGroup.gamedaysUrl.value;
    const location = useLocation()
    const isAmateurSoccerGroupNewUrl = location.pathname.includes('/amateurSoccerGroups/new');
    const playersUrl = menu.amateurSoccerGroup.playersUrl.value;

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
                                <Link to="#"
                                      className="nav-link dropdown-toggle"
                                      role="button"
                                      data-bs-toggle="dropdown"
                                      aria-haspopup="true"
                                      aria-expanded="false">
                                    Gamedays
                                </Link>
                                <ul className="dropdown-menu">
                                    <Link to={"/amateurSoccerGroups/gamedays"}
                                          className={"dropdown-item"}>
                                        View Gameday
                                    </Link>
                                    {menu.amateurSoccerGroup.gamedaysCreationUrl.value &&
                                        <li>
                                            <Link to="/amateurSoccerGroups/gamedays/new"
                                                  className="dropdown-item">
                                                Register Gameday
                                            </Link>
                                        </li>}
                                    {menu.amateurSoccerGroup.calculateRankingUrl.value &&
                                        <li>
                                            <Link to="/amateurSoccerGroups/ranking"
                                                  className="dropdown-item">
                                                Calculate Ranking
                                            </Link>
                                        </li>}
                                </ul>
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