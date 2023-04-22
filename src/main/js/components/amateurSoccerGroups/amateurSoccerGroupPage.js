import {Link, Outlet} from "react-router-dom";

export function AmateurSoccerGroupPage() {
    return <main>
        <nav>
            <Link to="/amateurSoccerGroups/">List</Link> |{" "}
            <Link to="/amateurSoccerGroups/new">New</Link>
        </nav>
        <Outlet/>
    </main>;
}