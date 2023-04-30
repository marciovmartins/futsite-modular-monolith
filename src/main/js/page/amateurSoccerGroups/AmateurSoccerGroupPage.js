import React, {useState} from "react";
import {Link} from "react-router-dom";
import {AmateurSoccerGroupList} from "../../components/amateurSoccerGroups/AmateurSoccerGroupList";
import {AmateurSoccerGroupView} from "../../components/amateurSoccerGroups/AmateurSoccerGroupView";

export function AmateurSoccerGroupPage() {
    const [amateurSoccerGroupLink, setAmateurSoccerGroupLink] = useState()

    return <main>
        <nav>
            <Link to="/amateurSoccerGroups"
                  onClick={() => setAmateurSoccerGroupLink(undefined)}
            >List</Link> |{" "}
            <Link to="/amateurSoccerGroups/new">New</Link>
        </nav>

        {!amateurSoccerGroupLink &&
            <AmateurSoccerGroupList callbackViewAmateurSoccerGroup={setAmateurSoccerGroupLink}/>}
        {amateurSoccerGroupLink &&
            <AmateurSoccerGroupView uri={amateurSoccerGroupLink}/>}
    </main>;
}