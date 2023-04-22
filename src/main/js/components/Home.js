import React from "react";
import {Link, Outlet} from "react-router-dom";

export default function Home() {
    return (
        <div>
            <nav>
                <Link to="/">Home</Link>&nbsp;|&nbsp;
                <Link to="/amateurSoccerGroups">Game Day</Link>&nbsp;|&nbsp;
            </nav>
            <Outlet/>
        </div>
    )
}