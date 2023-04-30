import React from "react";
import {Link, Outlet} from "react-router-dom";

export default function App() {
    return (
        <div>
            <nav>
                <Link to="/">Home</Link>&nbsp;|&nbsp;
                <Link to="/amateurSoccerGroups">Amateur Soccer Groups</Link>&nbsp;|&nbsp;
            </nav>
            <Outlet/>
        </div>
    )
}
