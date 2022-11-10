import React from 'react';
import { ActionButton, Flex, Grid, SearchField } from '@adobe/react-spectrum';
import ShowMenu from '@spectrum-icons/workflow/ShowMenu';
import { UserProfile } from '../user-profile/UserProfile';

export const Header = ({ menuClicked }) => {
    return (
        <Grid
            areas={['menu search profile']}
            columns={['80px auto 80px']}
            rows={['auto']}
            height="100%"
            width="100%"
            alignItems="center"
        >
            <ActionButton
                height="100%"
                width="100%"
                isQuiet
                onPress={menuClicked}
                gridArea="menu"
                data-testid="menu-area"
                alignItems="center"
                justifyContent="center"
            >
                <ShowMenu width="30px" height="30px" />
            </ActionButton>
            <Flex
                gridArea="search"
                justifyContent="center"
                data-testid="search-area"
            >
                <SearchField width="size-6000"></SearchField>
            </Flex>
            <ActionButton
                height="100%"
                width="100%"
                isQuiet
                gridArea="profile"
                data-testid="profile-area"
                alignItems="center"
                justifyContent="center"
            >
                <UserProfile />
            </ActionButton>
        </Grid>
    );
};
