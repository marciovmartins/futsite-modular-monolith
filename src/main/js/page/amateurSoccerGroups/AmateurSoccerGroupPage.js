import React, {useContext, useEffect, useState} from "react";
import {Outlet, Route, Routes, useNavigate} from "react-router-dom";

import {MenuContext} from "../App";
import {AmateurSoccerGroupMenu} from "./AmateurSoccerGroupMenu";
import {AmateurSoccerGroupView} from "../../components/amateurSoccerGroups/AmateurSoccerGroupView";
import {AmateurSoccerGroupNew} from "../../components/amateurSoccerGroups/AmateurSoccerGroupNew";
import {CalculateRanking} from "../../components/amateurSoccerGroups/CalculateRanking";
import {PlayerList} from "../../components/amateurSoccerGroups/PlayerList";
import {PlayerNew} from "../../components/amateurSoccerGroups/PlayerNew";
import {GamedayList} from "../../components/amateurSoccerGroups/GamedayList";
import {GamedayNew} from "../../components/amateurSoccerGroups/GamedayNew";
import {fetchGraphQL} from "../../api/fetchGraphQL";

export function AmateurSoccerGroupPage() {
    const navigate = useNavigate()
    const menu = useContext(MenuContext)
    const amateurSoccerGroupId = menu.amateurSoccerGroup.id.value;

    const [, setGamedayId] = useState()
    useEffect(() => {
        if (amateurSoccerGroupId === undefined) return;

        fetchGraphQL(`{
          amateurSoccerGroupById(id: "${amateurSoccerGroupId}") {
            id
            userData {
              name
            }
            actions
          }
        }`)
            .then(response => response.data.amateurSoccerGroupById)
            .then(amateurSoccerGroup => {
                const actions = Object.values(amateurSoccerGroup.actions)
                menu.amateurSoccerGroup.gamedaysCreationUrl.set(actions.filter(action => action === "create-gameday").length === 1)
                menu.amateurSoccerGroup.gamedaysUrl.set(actions.filter(action => action === "get-gamedays").length === 1)
                menu.amateurSoccerGroup.calculateRankingUrl.set(actions.filter(action => action === "calculate-ranking").length === 1)
                menu.amateurSoccerGroup.playersUrl.set(actions.filter(action => action === "get-players").length === 1)
            })
    }, [amateurSoccerGroupId]);

    return <main>
        <AmateurSoccerGroupMenu menu={menu}/>
        <div className={"m-3"}>
            <Outlet/>
        </div>
        <Routes>
            <Route path="view" element={
                <AmateurSoccerGroupView
                    id={amateurSoccerGroupId}
                />
            }/>

            <Route path="new" element={
                <AmateurSoccerGroupNew
                    setCreatedAmateurSoccerGroupId={(id) => {
                        menu.amateurSoccerGroup.id.set(id)
                        navigate('/amateurSoccerGroups/view')
                    }}
                />
            }/>

            <Route path="gamedays" element={
                <GamedayList
                    amateurSoccerGroupId={amateurSoccerGroupId}
                    creationRedirectWhenEmptyUrl={"/amateurSoccerGroups/gamedays/new"}
                />
            }/>

            <Route path="gamedays/new" element={
                <GamedayNew
                    setViewId={(gamedayId) => {
                        setGamedayId(gamedayId)
                        navigate("/amateurSoccerGroups/gamedays")
                    }}
                    amateurSoccerGroupId={amateurSoccerGroupId}
                    playersCreationUrl={"/amateurSoccerGroups/players/new"}
                />
            }/>

            <Route path="ranking" element={
                <CalculateRanking
                    amateurSoccerGroupId={menu.amateurSoccerGroup.id.value}
                />
            }/>

            <Route path="players" element={
                <PlayerList
                    amateurSoccerGroupId={amateurSoccerGroupId}
                    toPlayerNew={() => navigate("/amateurSoccerGroups/players/new")}
                />
            }/>

            <Route path="players/new" element={
                <PlayerNew
                    toPlayerList={() => navigate("/amateurSoccerGroups/players")}
                />
            }/>
        </Routes>
    </main>;
}
