import React, {useState} from "react";
import {Link, Outlet, Route, Routes} from "react-router-dom";
import {AmateurSoccerGroupList} from "../../components/amateurSoccerGroups/AmateurSoccerGroupList";
import {AmateurSoccerGroupView} from "../../components/amateurSoccerGroups/AmateurSoccerGroupView";
import {AmateurSoccerGroupNew} from "../../components/amateurSoccerGroups/AmateurSoccerGroupNew";

export function AmateurSoccerGroupPage() {
    const [viewLink, setViewLink] = useState()
    const [creationLink, setCreationLink] = useState()

    return <main>

        <nav>
            <Link to="/amateurSoccerGroups"
                  onClick={() => setViewLink(undefined)}
            >List</Link> |{" "}

            {creationLink &&
                <Link to="/amateurSoccerGroups/new">New</Link>}
        </nav>

        <Outlet/>
        <Routes>
            <Route index element={
                !viewLink && <AmateurSoccerGroupList
                    setViewLink={setViewLink}
                    setCreationLink={setCreationLink}/>
                || <AmateurSoccerGroupView
                    uri={viewLink}/>
            }/>
            <Route path="new" element={<AmateurSoccerGroupNew
                creationLink={creationLink}
                setViewLink={setViewLink}
            />}/>
        </Routes>
    </main>;
}