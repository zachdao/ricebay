import React, { useCallback, useState } from 'react';
import {
    ActionButton,
    Button,
    ButtonGroup,
    Flex,
    Grid,
    Heading,
    Text,
    TextField,
    View,
} from '@adobe/react-spectrum';
import { UserProfile } from '../../user-profile/UserProfile';
import SaveFloppy from '@spectrum-icons/workflow/SaveFloppy';
import toast from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';
import { Popover } from '../../popover/Popover';
import { usePostWithToast } from '../../http-query/use-post-with-toast';
import JumpToTop from '@spectrum-icons/workflow/JumpToTop';
import axios from "axios";

export const AccountEdit = ({ account }) => {
    const [givenName, setGivenName] = useState(account.givenName || '');
    const [surname, setSurname] = useState(account.surname || '');
    const [alias, setAlias] = useState(account.alias || '');
    const [email, setEmail] = useState(account.email || '');
    const [zelleId, setZelleId] = useState(account.zelleId || '');
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState({});
    const [changePassword, setChangePassword] = useState(false);
    const navigate = useNavigate();

    const undo = usePostWithToast(
        `/accounts/${account.id}`,
        account,
        [account],
        { message: 'Account details restored to previous values' },
        { message: 'Failed to undo!' },
        (appResponse) => {
            setGivenName(appResponse.data.givenName);
            setSurname(appResponse.data.surname);
            setAlias(appResponse.data.alias);
            setEmail(appResponse.data.email);
            setZelleId(appResponse.data.zelleId);
            setError({});
        },
        (axiosError) => setError(axiosError?.response?.data || {}),
    );

    const save = usePostWithToast(
        `/accounts/${account.id}`,
        {
            ...account,
            givenName,
            surname,
            alias,
            email,
            zelleId,
        },
        [givenName, surname, alias, email, zelleId],
        {
            message: 'Account details updated!',
            actionTitle: 'UNDO',
            actionFn: (t) => {
                undo();
                toast.remove(t.id);
            },
        },
        { message: 'Failed to save account' },
        null,
        (axiosError) => setError(axiosError?.response?.data || {}),
    );

    const updatePassword = usePostWithToast(
        `/accounts/${account.id}/password`,
        { currentPassword, newPassword },
        [currentPassword, newPassword],
        { message: 'Password updated!' },
        { message: 'Failed to update password' },
        () => {
            setCurrentPassword('');
            setNewPassword('');
            setConfirmPassword('');
            setChangePassword(false);
            setError({});
        },
        (axiosError) => setError(axiosError?.response?.data || {}),
    );

    const isClean = useCallback(() => {
        return (
            givenName === account.givenName &&
            surname === account.surname &&
            alias === account.alias &&
            email === account.email &&
            zelleId === account.zelleId
        );
    }, [givenName, surname, alias, email, zelleId]);

    const accountOverview = useCallback(async () => {
        await axios.post('/accounts/edit');
        navigate('/account');
    }, []);

    return (
        <Grid
            areas={['image form']}
            columns={['1fr', '2fr']}
            alignItems="start"
            justifyContent="start"
            columnGap="20px"
        >
            <View gridArea="image" position="relative" maxWidth="250px">
                <UserProfile src={account.image} />
                {/*TODO: Tie to action to upload profile image*/}
                <ActionButton
                    isQuiet
                    bottom="20px"
                    right="20px"
                    position="absolute"
                >
                    <JumpToTop />
                </ActionButton>
            </View>
            <Flex
                direction="column"
                justifyContent="center"
                width="100%"
                rowGap="10px"
                gridArea="form"
            >
                <Heading level="2">Personal Information *</Heading>
                <Flex
                    direction="row"
                    alignItems="center"
                    width="100%"
                    columnGap="15px"
                >
                    <TextField
                        label="First Name"
                        value={givenName}
                        onChange={(value) => {
                            setGivenName(value);
                            setError({});
                        }}
                        validationState={
                            error.givenName ? 'invalid' : undefined
                        }
                        errorMessage={error.givenName}
                    />
                    <TextField
                        label="Last Name"
                        value={surname}
                        onChange={(value) => {
                            setSurname(value);
                            setError({});
                        }}
                        validationState={error.surname ? 'invalid' : undefined}
                        errorMessage={error.surname}
                    />
                    <TextField
                        label="Alias"
                        value={alias}
                        onChange={(value) => {
                            setAlias(value);
                            setError({});
                        }}
                        validationState={error.alias ? 'invalid' : undefined}
                        errorMessage={error.alias}
                    />
                </Flex>
                <TextField
                    label="Email"
                    value={email}
                    onChange={(value) => {
                        setEmail(value);
                        setError({});
                    }}
                    validationState={error.email ? 'invalid' : undefined}
                    errorMessage={error.email}
                    width="100%"
                />
                <Popover
                    triggerText="Change Password"
                    isOpen={changePassword}
                    onPress={() => setChangePassword((value) => !value)}
                >
                    <Flex
                        direction="column"
                        justifyContent="center"
                        alignItems="center"
                        gap="10px"
                        margin="15px"
                        width="80%"
                    >
                        <TextField
                            type="password"
                            label="Current Password"
                            isRequired
                            value={currentPassword}
                            onChange={setCurrentPassword}
                            width="100%"
                        />
                        <TextField
                            type="password"
                            label="New Password"
                            isRequired
                            value={newPassword}
                            onChange={setNewPassword}
                            validationState={
                                error.password ? 'invalid' : undefined
                            }
                            errorMessage={error.password}
                            width="100%"
                        />
                        <TextField
                            type="password"
                            label="Confirm Password"
                            isRequired
                            value={confirmPassword}
                            onChange={setConfirmPassword}
                            width="100%"
                        />
                        <ButtonGroup width="100%" align="center">
                            <Button
                                variant="negative"
                                onPress={() => {
                                    setCurrentPassword('');
                                    setNewPassword('');
                                    setConfirmPassword('');
                                    setChangePassword(false);
                                    setError({});
                                }}
                            >
                                Cancel
                            </Button>
                            <Button
                                variant="cta"
                                onPress={() => {
                                    updatePassword();
                                }}
                                isDisabled={
                                    !currentPassword ||
                                    !newPassword ||
                                    newPassword !== confirmPassword
                                }
                            >
                                Set Password
                            </Button>
                        </ButtonGroup>
                    </Flex>
                </Popover>
                <Heading level="2">Payment Information *</Heading>
                <TextField
                    type="password"
                    label="Zelle Account ID"
                    value={zelleId}
                    onChange={(value) => {
                        setZelleId(value);
                        setError({});
                    }}
                    validationState={error.zelleId ? 'invalid' : undefined}
                    errorMessage={error.zelleId}
                />
                <ButtonGroup width="100%" align="center" marginTop="20px">
                    <Button
                        variant="negative"
                        minWidth="100px"
                        isDisabled={changePassword}
                        onPress={() => navigate(-1)}
                    >
                        Cancel
                    </Button>
                    <Button
                        variant="cta"
                        minWidth="100px"
                        onPress={save}
                        isDisabled={isClean() || changePassword}
                    >
                        <SaveFloppy />
                        <Text>Save</Text>
                    </Button>
                    <Button
                        variant="cta"
                        minWidth="100px"
                        onPress={accountOverview}
                    >
                        Account Overview
                    </Button>
                </ButtonGroup>
            </Flex>
        </Grid>
    );
};
