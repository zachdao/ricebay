import React, { useCallback, useContext } from 'react';
import {
    ActionButton,
    Content,
    Dialog,
    Divider,
    Flex,
    Grid,
    Heading,
    SearchField,
    Text,
    DialogTrigger,
    ButtonGroup,
    Button,
} from '@adobe/react-spectrum';
import ShowMenu from '@spectrum-icons/workflow/ShowMenu';
import { UserProfile } from '../user-profile/UserProfile';
import LogOut from '@spectrum-icons/workflow/LogOut';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { UserContext } from '../user.context';

export const Header = ({ menuClicked }) => {
    const navigate = useNavigate();
    const user = useContext(UserContext);
    const logout = useCallback(async () => {
        await axios.post('/accounts/logout');
        navigate('/login');
    }, []);
    const create = useCallback(async () => {
        navigate('/create');
    }, []);
    return (
        <Grid
            areas={['menu search create profile']}
            columns={['80px auto  130px 80px']}
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
            <Button
                variant="cta"
                gridArea="create"
                onPress={create}
                data-testid="create-area"
            >
                Create Auction
            </Button>

            <DialogTrigger type="popover">
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
                <Dialog width="max-content">
                    <Content>
                        <Flex
                            direction="column"
                            alignItems="center"
                            justifyContent="center"
                            gap="size-100"
                        >
                            <Heading>{user?.alias}</Heading>
                            <Text>{user?.email}</Text>
                            <Button variant="primary" isQuiet onPress={logout}>
                                <LogOut />
                                <Text>Sign Out</Text>
                            </Button>
                        </Flex>
                    </Content>
                </Dialog>
            </DialogTrigger>
        </Grid>
    );
};
