import React, {useState} from "react";
import {Link, Outlet, Route, Routes, useLocation} from "react-router-dom";
import {AmateurSoccerGroupList} from "../../components/amateurSoccerGroups/AmateurSoccerGroupList";
import {AmateurSoccerGroupView} from "../../components/amateurSoccerGroups/AmateurSoccerGroupView";
import {AmateurSoccerGroupNew} from "../../components/amateurSoccerGroups/AmateurSoccerGroupNew";
import {CalculateRanking} from "../../components/amateurSoccerGroups/CalculateRanking";

export function AmateurSoccerGroupPage() {
    const state = useLocation().state
    const [viewLink, setViewLink] = useState(state?.amateurSoccerGroupLink)
    const [creationLink, setCreationLink] = useState()
    const [gamedaysLink, setGamedaysLink] = useState()
    const [calculateRankingLink, setCalculateRankingLink] = useState()

    return <main>

        <nav>
            <Link to="/amateurSoccerGroups">List</Link>

            {creationLink &&
                <Link to="/amateurSoccerGroups/new"> | New</Link>}

            {gamedaysLink &&
                <Link to={"/gamedays"} state={{gamedaysLink}}> | Gamedays</Link>}

            {calculateRankingLink &&
                <Link to={"/amateurSoccerGroups/ranking"}> | Ranking</Link>}
        </nav>

        <Outlet/>
        <Routes>
            <Route index element={
                <AmateurSoccerGroupList
                    setViewLink={setViewLink}
                    setCreationLink={setCreationLink}
                />
            }/>

            <Route path="view" element={
                <AmateurSoccerGroupView
                    uri={viewLink}
                    setGamedaysLink={setGamedaysLink}
                    setCalculateRankingLink={setCalculateRankingLink}
                />
            }/>

            <Route path="new" element={
                <AmateurSoccerGroupNew
                    creationLink={creationLink}
                    setViewLink={setViewLink}
                />
            }/>

            <Route path="ranking" element={
                <CalculateRanking
                    uri={calculateRankingLink}
                />
            }/>
        </Routes>
    </main>;
}