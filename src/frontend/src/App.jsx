import React, { useState } from 'react';
import { Grid, View } from '@adobe/react-spectrum';
import { Header } from './header/Header';
import { Sidebar } from './sidebar/Sidebar';

export const App = () => {
    const [showSidebar, setShowSidebar] = useState(false);

    return (
        <Grid
            areas={['header header', 'sidebar content', 'footer footer']}
            columns={['1fr', '3fr']}
            rows={['size-1000', 'auto', 'size-1000']}
            height="100vh"
            backgroundColor="white"
        >
            <View backgroundColor="gray-300" gridArea="header">
                <Header
                    menuClicked={() => setShowSidebar(prev => !prev)}
                ></Header>
            </View>
            {showSidebar && (
                <View gridArea="sidebar" height="100%">
                    <Sidebar />
                </View>
            )}
            <View
                backgroundColor="gray-100"
                gridColumnStart={showSidebar ? 'content' : 'sidebar'}
                gridColumnEnd="content"
            ></View>
            <View backgroundColor="blue-700" gridArea="footer"></View>
        </Grid>
    );
};
