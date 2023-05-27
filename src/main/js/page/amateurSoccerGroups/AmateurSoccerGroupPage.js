import React, {useContext} from "react";
import {Outlet, Route, Routes, useNavigate} from "react-router-dom";
import {AmateurSoccerGroupMenu} from "./AmateurSoccerGroupMenu";
import {AmateurSoccerGroupView} from "../../components/amateurSoccerGroups/AmateurSoccerGroupView";
import {AmateurSoccerGroupNew} from "../../components/amateurSoccerGroups/AmateurSoccerGroupNew";
import {CalculateRanking} from "../../components/amateurSoccerGroups/CalculateRanking";
import {AmateurSoccerGroupsLoadContext, MenuContext} from "../App";
import {useSessionState} from "../../api/useSessionState";

export function AmateurSoccerGroupPage() {
    const navigate = useNavigate()
    const menu = useContext(MenuContext)
    const amateurSoccerGroupLoadContext = useContext(AmateurSoccerGroupsLoadContext)
    const [userDataCreationUrl] = useSessionState("amateurSoccerGroupUserDataCreationUrl") //TODO: weird! Should be replace if used GraphQL

    return <main>
        <AmateurSoccerGroupMenu menu={menu}/>
        <Outlet/>
        <Routes>
            <Route path="view" element={
                <AmateurSoccerGroupView
                    url={menu.amateurSoccerGroup.viewUrl.value}
                    setGamedaysUrl={menu.amateurSoccerGroup.gamedaysUrl.set}
                    setCalculateRankingUrl={menu.amateurSoccerGroup.calculateRankingUrl.set}
                />
            }/>

            <Route path="new" element={
                <AmateurSoccerGroupNew
                    creationUrl={menu.amateurSoccerGroup.creationUrl.value}
                    userDataCreationUrl={userDataCreationUrl}
                    setCreatedAmateurSoccerGroupUrl={(link) => {
                        menu.amateurSoccerGroup.viewUrl.set(link)
                        amateurSoccerGroupLoadContext.set(true)
                        navigate('/amateurSoccerGroups/view')
                    }}
                />
            }/>

            <Route path="ranking" element={
                <CalculateRanking
                    url={menu.amateurSoccerGroup.calculateRankingUrl.value}
                />
            }/>
        </Routes>
    </main>;
}
