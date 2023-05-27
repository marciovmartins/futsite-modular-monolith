import React from "react";
import {Link} from "react-router-dom";

export function AmateurSoccerGroupMenu(
    {menu}
) {
    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
            <Link to="/amateurSoccerGroups/view" className="navbar-brand">Amateur Soccer Group</Link>
            <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarNav">
                <ul className="navbar-nav">
                    {menu.amateurSoccerGroup.gamedaysUrl.value &&
                        <li className="nav-item">
                            <Link to={"/gamedays"} className="nav-link">Gamedays</Link>
                        </li>}
                    {menu.amateurSoccerGroup.calculateRankingUrl.value &&
                        <li className="nav-item">
                            <Link to={"/amateurSoccerGroups/ranking"} className="nav-link">Ranking</Link>
                        </li>}
                </ul>
            </div>
        </nav>
    )
}